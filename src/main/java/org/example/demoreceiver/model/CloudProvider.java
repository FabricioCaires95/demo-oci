package org.example.demoreceiver.model;

public enum CloudProvider {

    OCI(1, "OCI"),
    AZURE(2, "AZURE"),
    AWS(3, "AWS");

    private Integer cod;
    private String name;

    CloudProvider(Integer cod, String name) {
        this.cod = cod;
        this.name = name;
    }

    public static String getCloudProvider(Integer cloudProviderId) {
        for (CloudProvider cloudProvider : CloudProvider.values()) {
            if (cloudProvider.getCod().intValue() == cloudProviderId) {
                return cloudProvider.getName();
            }
        }

        throw new IllegalArgumentException("No CloudProvider found for cod: " + cloudProviderId);
    }

    public String getName() {
        return name;
    }

    public Integer getCod() {
        return cod;
    }
}
