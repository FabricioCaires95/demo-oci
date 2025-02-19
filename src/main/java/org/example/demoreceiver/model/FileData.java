package org.example.demoreceiver.model;

public record FileData(String callback, String requestSytemModule, Data data, Boolean possibleDuplication, String requester) {
}
