package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.Entities.Role;
import com.ecommerce.WatchStore.Services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping("/create")
    public ResponseEntity<String> createRole(@RequestBody Role role) {
        Role existingRole = roleService.getRoleByTitle(role.getRoleTitle());
        if (existingRole != null) {
            return new ResponseEntity<>("Vai trò đã tồn tại.", HttpStatus.BAD_REQUEST);
        }

        Role newRole = roleService.createRole(role);
        return new ResponseEntity<>("Vai trò đã được tạo thành công.", HttpStatus.CREATED);
    }
}
