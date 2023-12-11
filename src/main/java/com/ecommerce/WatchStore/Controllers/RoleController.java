package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.Entities.Role;
import com.ecommerce.WatchStore.Services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/Admin/Roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping("/Create")
    public ResponseEntity<String> createRole(@RequestBody Role role) {
        Role existingRole = roleService.getRoleByTitle(role.getRoleTitle());
        if (existingRole != null) {
            return new ResponseEntity<>("Vai trò đã tồn tại.", HttpStatus.BAD_REQUEST);
        }

        Role newRole = roleService.createRole(role);
        return new ResponseEntity<>("Vai trò đã được tạo thành công.", HttpStatus.CREATED);
    }

    @PutMapping("/Update") // Sử dụng PutMapping để thực hiện việc cập nhật
    public ResponseEntity<String> updateRole( @RequestBody Role role) {
        Role updatedRole = roleService.updateRole(role);
        if (updatedRole != null) {
            return new ResponseEntity<>("Vai trò đã được cập nhật thành công.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Không thể cập nhật vai trò.", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/Delete/{id}") // Sử dụng DeleteMapping để thực hiện việc xóa
    public ResponseEntity<String> deleteRole(@PathVariable Long id) {
        try {
            roleService.deleteRole(id);
            return new ResponseEntity<>("Vai trò đã được xóa thành công.", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
