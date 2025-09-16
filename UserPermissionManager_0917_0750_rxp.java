// 代码生成时间: 2025-09-17 07:50:42
 * for maintainability and scalability.
 */

import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserPermissionManager {

    private static final Map<String, Map<String, Boolean>> userPermissions = new ConcurrentHashMap<>();

    // Initialize Javalin and define routes
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);

        EndpointGroup userEndpoints = app.group("/users");

        // Get all user permissions
        userEndpoints.get("/permissions", ctx -> {
            ctx.json(userPermissions);
        });

        // Set user permission
        userEndpoints.post("/permissions", ctx -> {
            String username = ctx.bodyAsClass(UserPermissionRequest.class).getUsername();
            String permissionKey = ctx.bodyAsClass(UserPermissionRequest.class).getPermissionKey();
            boolean permissionValue = ctx.bodyAsClass(UserPermissionRequest.class).isPermissionValue();

            userPermissions.computeIfAbsent(username, k -> new HashMap<>()).put(permissionKey, permissionValue);
            ctx.status(201).json("Permission updated for user: " + username);
        });

        // Delete user permission
        userEndpoints.delete("/permissions/:username/:permissionKey", ctx -> {
            String username = ctx.pathParam("username");
            String permissionKey = ctx.pathParam("permissionKey");

            if (userPermissions.containsKey(username) && userPermissions.get(username).remove(permissionKey) != null) {
                ctx.status(200).json("Permission removed for user: " + username);
            } else {
                ctx.status(404).json("User or permission not found");
            }
        });
    }

    // Request class for user permissions
    public static class UserPermissionRequest {
        private String username;
        private String permissionKey;
        private boolean permissionValue;

        // Getters and setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPermissionKey() {
            return permissionKey;
        }

        public void setPermissionKey(String permissionKey) {
            this.permissionKey = permissionKey;
        }

        public boolean isPermissionValue() {
            return permissionValue;
        }

        public void setPermissionValue(boolean permissionValue) {
            this.permissionValue = permissionValue;
        }
    }
}
