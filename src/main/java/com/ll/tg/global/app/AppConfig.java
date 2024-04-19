package com.ll.tg.global.app;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private static String activeProfile;

    @Value("${spring.profiles.active}")
    public void setActiveProfile(String activeProfile) {
        this.activeProfile = activeProfile;
    }

    public static boolean isProd() {
        return activeProfile.equals("prod");
    }

    public static boolean isDev() {
        return activeProfile.equals("dev");
    }

    public static boolean isTest() {
        return activeProfile.equals("Test");
    }

    public static boolean isNotProd() {
        return !isProd();
    }

    @Getter
    private static String jwtSecretKey;

    @Value("${custom.jwt.secretKey}")
    public void setJwtSecretKey(String jwtSecretKey) {
        this.jwtSecretKey = jwtSecretKey;
    }

    @Getter
    public static String tempDirPath;

    @Value("${custom.temp.dirPath}")
    public void setTempDirPath(String tempDirPath) {
        this.tempDirPath = tempDirPath;
    }

    @Getter
    public static String genDirPath;

    @Value("${custom.gen.dirPath}")
    public void setGenDirPath(String genDirPath) {
        this.genDirPath = genDirPath;
    }
}