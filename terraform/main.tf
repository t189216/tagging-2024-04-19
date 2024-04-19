terraform {
  # AWS 라이브러리 import
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.0"
    }
  }
}

# AWS 설정 시작
provider "aws" {
  region = var.region
}
# AWS 설정 끝

# VPC 설정 시작
resource "aws_vpc" "vpc_1" {
  cidr_block = "10.0.0.0/16"

  # DNS 지원을 활성화
  enable_dns_support = true
  # DNS 호스트 이름 지정을 활성화
  enable_dns_hostnames = true

  tags = {
    Name = "${var.prefix}-vpc-1"
  }
}

resource "aws_subnet" "subnet_1" {
  vpc_id                  = aws_vpc.vpc_1.id
  cidr_block              = "10.0.1.0/24"
  availability_zone       = "${var.region}a"
  map_public_ip_on_launch = true # 이 서브넷이 배포되는 인스턴스에 공용 IP를 자동으로 할당

  tags = {
    Name = "${var.prefix}-subnet-1"
  }
}

resource "aws_subnet" "subnet_2" {
  vpc_id                  = aws_vpc.vpc_1.id
  cidr_block              = "10.0.2.0/24"
  availability_zone       = "${var.region}b"
  map_public_ip_on_launch = true

  tags = {
    Name = "${var.prefix}-subnet-2"
  }
}

resource "aws_subnet" "subnet_3" {
  vpc_id                  = aws_vpc.vpc_1.id
  cidr_block              = "10.0.3.0/24"
  availability_zone       = "${var.region}c"
  map_public_ip_on_launch = true

  tags = {
    Name = "${var.prefix}-subnet-3"
  }
}

# AWS 인터넷 게이트웨이
resource "aws_internet_gateway" "igw_1" {
  vpc_id = aws_vpc.vpc_1.id

  tags = {
    Name = "${var.prefix}-igw-1"
  }
}

# AWS 라우트 테이블
resource "aws_route_table" "rt_1" {
  vpc_id = aws_vpc.vpc_1.id

  # 라우트 규칙을 설정. 모든 트래픽(0.0.0.0/0)을 'igw_1' 인터넷 게이트웨이로 보냄
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw_1.id
  }

  tags = {
    Name = "${var.prefix}-rt-1"
  }
}

# 라우트 테이블과 서브넷을 연결
resource "aws_route_table_association" "association_1" {
  subnet_id      = aws_subnet.subnet_1.id
  route_table_id = aws_route_table.rt_1.id
}

resource "aws_route_table_association" "association_2" {
  subnet_id      = aws_subnet.subnet_2.id
  route_table_id = aws_route_table.rt_1.id
}

resource "aws_route_table_association" "association_3" {
  subnet_id      = aws_subnet.subnet_3.id
  route_table_id = aws_route_table.rt_1.id
}

# AWS 보안 그룹 리소스
resource "aws_security_group" "sg_1" {
  name = "${var.prefix}-sg-1"

  # 인바운드 트래픽 규칙
  ingress {
    from_port = 0
    to_port   = 0
    protocol = "all"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # 아웃바운드 트래픽 규칙
  egress {
    from_port = 0
    to_port   = 0
    protocol = "all"
    cidr_blocks = ["0.0.0.0/0"]
  }

  vpc_id = aws_vpc.vpc_1.id

  tags = {
    Name = "${var.prefix}-sg-1"
  }
}

# EC2 설정 시작

# EC2 역할 생성
resource "aws_iam_role" "ec2_role_1" {
  name = "${var.prefix}-ec2-role-1"

  # 이 역할에 대한 신뢰 정책 설정. EC2 서비스가 이 역할을 가정할 수 있도록 설정
  assume_role_policy = <<EOF
  {
    "Version": "2012-10-17",
    "Statement": [
      {
        "Sid": "",
        "Action": "sts:AssumeRole",
        "Principal": {
            "Service": "ec2.amazonaws.com"
        },
        "Effect": "Allow"
      }
    ]
  }
  EOF
}

# EC2 역할에 AmazonS3FullAccess 정책을 부착
resource "aws_iam_role_policy_attachment" "s3_full_access" {
  role       = aws_iam_role.ec2_role_1.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonS3FullAccess"
}

# EC2 역할에 AmazonEC2RoleforSSM 정책을 부착
resource "aws_iam_role_policy_attachment" "ec2_ssm" {
  role       = aws_iam_role.ec2_role_1.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonEC2RoleforSSM"
}

# IAM 인스턴스 프로파일 생성
resource "aws_iam_instance_profile" "instance_profile_1" {
  name = "${var.prefix}-instance-profile-1"
  role = aws_iam_role.ec2_role_1.name
}

locals {
  ec2_user_data_base = <<-END_OF_FILE
#!/bin/bash
yum install docker -y    # Docker 설치
systemctl enable docker  # Docker 부팅 시 자동 시작 설정
systemctl start docker   # Docker 서비스 시작
curl -L https:#github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose  # 최신 Docker Compose 다운로드 및 설치
chmod +x /usr/local/bin/docker-compose  # Docker Compose 실행 권한 부여
yum install git -y  # Git 설치
sudo dd if=/dev/zero of=/swapfile bs=128M count=32  # 4GB 스왑 파일 생성
sudo chmod 600 /swapfile  # 스왑 파일 권한 변경
sudo mkswap /swapfile  # 스왑 파일을 스왑 공간으로 설정
sudo swapon /swapfile  # 스왑 파일 활성화
sudo swapon -s  # 활성화된 스왑 파일 목록 확인
sudo sh -c 'echo "/swapfile swap swap defaults 0 0" >> /etc/fstab'  # 부팅 시 스왑 파일 자동 활성화 설정
END_OF_FILE
}

# EC2 인스턴스 생성
resource "aws_instance" "ec2_1" {
  ami                         = "ami-0c031a79ffb01a803" # Amazon Linux 2023 AMI
  instance_type               = "t2.micro"
  subnet_id                   = aws_subnet.subnet_1.id
  vpc_security_group_ids      = [aws_security_group.sg_1.id]
  associate_public_ip_address = true # 퍼블릭 IP 연결 설정

  # 인스턴스에 IAM 역할 연결
  iam_instance_profile = aws_iam_instance_profile.instance_profile_1.name

  tags = {
    Name = "${var.prefix}-ec2-1"
  }

  # 루트 볼륨 설정
  root_block_device {
    volume_type = "gp3"
    volume_size = 32
  }

  # User data script for ec2_1
  user_data = <<-EOF
${local.ec2_user_data_base}
mkdir -p /docker_projects/tagging/source
cd /docker_projects/tagging/source
git clone https://github.com/T189216/tagging .
# 도커 이미지 생성
docker build -t tagging_1:1 .
# 생성된 이미지 실행
docker run \
    --name=tagging_1_1 \
    -p 8080:8080 \
    -v /docker_projects/tagging_1/volumes/gen:/gen \
    --restart unless-stopped \
    -e TZ=Asia/Seoul \
    -d \
    tagging_1:1
EOF
}