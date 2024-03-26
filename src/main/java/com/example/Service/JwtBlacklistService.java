package com.example.Service;

public interface JwtBlacklistService {
    void addToBlacklist(String token);
    boolean isBlacklisted(String token);
}
