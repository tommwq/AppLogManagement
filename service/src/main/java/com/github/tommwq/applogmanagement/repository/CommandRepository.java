package com.github.tommwq.applogmanagement.repository;

import com.github.tommwq.applogmanagement.AppLogManagementProto.Command;
import java.util.List;

/**
 * Cache log query command to offline devices.
 */
public interface CommandRepository {
        void save(Command log);
        void remove(Command log);
        List<Command> loadAll();
}
