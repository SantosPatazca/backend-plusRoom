package com.ertedemo.shared.services.impl;

import com.ertedemo.domain.model.entites.RoomiePreference;
import com.ertedemo.domain.model.entites.User;
import com.ertedemo.domain.persistence.RoomiePreferenceRepository;
import com.ertedemo.domain.persistence.UserRepository;
import com.ertedemo.domain.services.RoomieMatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomieMatchingServiceImpl implements RoomieMatchingService {

    @Autowired
    private RoomiePreferenceRepository roomiePreferenceRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> findMatches(User user) {
        RoomiePreference userPreferences = roomiePreferenceRepository.findByUserId(user.getId());
        List<RoomiePreference> allPreferences = roomiePreferenceRepository.findAll();
        return allPreferences.stream()
                .filter(pref -> !pref.getUserId().equals(user.getId()))
                .filter(pref -> pref.getLocationPreference().equals(userPreferences.getLocationPreference()))
                .map(pref -> userRepository.findById(pref.getUserId()).orElse(null))
                .collect(Collectors.toList());
    }
}