package com.github.tommwq.applogmanagement.api;

import com.github.tommwq.applogmanagement.AppLogManagementProto.Command;
import java.util.List;

/**
 * Cache log query command to offline devices.
 */
public interface CommandRepositoryApi {

        public static class Command {
                public String command;

                public Command() {
                }
                
                public Command(com.github.tommwq.applogmanagement.AppLogManagementProto.Command cmd) {
                        command = cmd.toString();
                }
        }
        
        void save(com.github.tommwq.applogmanagement.AppLogManagementProto.Command log);
        void remove(com.github.tommwq.applogmanagement.AppLogManagementProto.Command log);
        List<com.github.tommwq.applogmanagement.AppLogManagementProto.Command> loadAll();
}
