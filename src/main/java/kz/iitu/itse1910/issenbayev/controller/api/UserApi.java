package kz.iitu.itse1910.issenbayev.controller.api;

public class UserApi {
    public static class Filter {
        public static final String ROLE = "role";
        public static final String IS_ASSIGNED_TO_PROJECT = "is-assigned-to-project";
    }

    public static class Role {
        public static final String DEVELOPER = "developer";
        public static final String LEAD_DEV = "lead-dev";
        public static final String MANAGER = "manager";
        public static final String ADMIN = "admin";
    }
}
