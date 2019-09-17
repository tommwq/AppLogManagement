package com.github.tommwq.applogmanagement.api;

import com.github.tommwq.applogmanagement.AppLogManagementProto.Command;
import java.util.List;

/**
 * Cache log query command to offline devices.
 */
public interface CommandRepositoryApi {

        public static class Command {
                String command;
                public Command(com.github.tommwq.applogmanagement.AppLogManagementProto.Command cmd) {
                        command = cmd.toString();
                }
        }
        
        void save(Command log);
        void remove(Command log);
        List<Command> loadAll();
}
