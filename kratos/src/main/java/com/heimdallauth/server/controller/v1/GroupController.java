package com.heimdallauth.server.controller.v1;

import com.heimdallauth.server.commons.dto.kratos.CreateDirectoryGroup;
import com.heimdallauth.server.commons.dto.kratos.CreateRoleMappingWithGroup;
import com.heimdallauth.server.commons.models.kratos.GroupModel;
import com.heimdallauth.server.services.GroupControllerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/groups")
public class GroupController {
    private final GroupControllerService groupControllerService;

    public GroupController(GroupControllerService groupControllerService) {
        this.groupControllerService = groupControllerService;
    }

    @PostMapping("/group")
    public ResponseEntity<GroupModel> createGroup(@RequestBody CreateDirectoryGroup createDirectoryGroupPayload) {
        return ResponseEntity.ok(this.groupControllerService.createNewGroup(createDirectoryGroupPayload));
    }

    @GetMapping
    public ResponseEntity<List<GroupModel>> getAllAvailableGroups() {
        return ResponseEntity.ok(this.groupControllerService.getAllGroups());
    }

    @PutMapping("/{groupId}/roles")
    public ResponseEntity<GroupModel> updateGroupRoleMapping(@PathVariable("groupId") String groupId, @RequestBody CreateRoleMappingWithGroup roleMappingWithGroup) {
        return ResponseEntity.ok(this.groupControllerService.updateGroupRoleMapping(groupId, roleMappingWithGroup.getRolesId()));
    }

    @PutMapping("/{groupId}/bulkRoles")
    public ResponseEntity<List<GroupModel>> updateBulkGroupRoleMappings(@RequestBody List<CreateRoleMappingWithGroup> roleMappings) {
//        return ResponseEntity.ok(this.groupControllerService.updateBulkGroupRoleMappings(roleMappings));
        return ResponseEntity.ok().build();
    }

}
