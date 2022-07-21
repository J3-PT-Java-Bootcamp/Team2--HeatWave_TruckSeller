package com.ironhack;

public class FakeLeadGenerator {
    public static void main(String[] args) {
        net.datafaker.Faker faker = new net.datafaker.Faker();
        java.io.FileWriter writer;
        try {
            writer = new java.io.FileWriter(".idea/import/rawLeadsData4.csv");
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }

        var sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            var company=faker.company().name().trim().split(",")[0];
            sb.append(faker.name().fullName());
            sb.append(",");
            sb.append(faker.phoneNumber().cellPhone());
            sb.append(",");
            sb.append(faker.name().username() + "@" + company.replace(" ","_") + ".com");
            sb.append(",");
            sb.append(company);
            sb.append("\n");
        }
        try {
            writer.write(sb.toString());
            writer.close();
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }
}
