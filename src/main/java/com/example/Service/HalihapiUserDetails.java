package com.example.Service;

import org.springframework.security.core.userdetails.UserDetails;

public interface HalihapiUserDetails extends UserDetails {
    int getUserID();
}
