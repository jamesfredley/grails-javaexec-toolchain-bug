package com.example;

public class ToolchainCheck {
    public static void main(String[] args) {
        String javaHome = System.getProperty("java.home");
        String javaVersion = System.getProperty("java.version");
        String expectedMajor = "17";

        boolean matchesToolchain = javaVersion != null
            && (javaVersion.equals(expectedMajor) || javaVersion.startsWith(expectedMajor + "."));

        System.out.println("java.home=" + javaHome);
        System.out.println("java.version=" + javaVersion);
        System.out.println("matchesToolchain=" + matchesToolchain);
    }
}
