package com.example.backendtesting.backend.api;

public interface CardManager {
    /**
     * Create a new card with the specified contents and answer
     */
    Card buildCard(Video video, String answer);
}
