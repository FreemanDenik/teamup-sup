package ru.team.up.sup.core.service;

import ru.team.up.sup.core.entity.Admin;

import java.util.List;

/**
 * @author Alexey Tkachenko
 */
public interface AdminService { // под снос!
    List<Admin> getAllAdmins();

    Admin getOneAdmin(Long id);

    Admin saveAdmin(Admin admin);

    void deleteAdmin(Long id);
}
