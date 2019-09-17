package com.github.tommwq.applogmanagement.provider;

import com.github.tommwq.applogmanagement.api.CommandRepositoryApi;
import com.github.tommwq.applogmanagement.AppLogManagementProto.Command;
import com.github.tommwq.utility.collection.MapUtil;
import com.github.tommwq.utility.database.SQLiteHelper;
import com.github.tommwq.utility.function.Call;
import com.github.tommwq.utility.function.Predicates;
import com.google.protobuf.TextFormat;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Cache log query command to offline devices.
 */
public class CommandRepository implements CommandRepositoryApi {

        private SQLiteHelper helper;
        public CommandRepository(Connection connection) {
                helper = new SQLiteHelper(connection);
                new Call(() -> helper.createTableInNeed(CommandRepositoryApi.Command.class)).rethrow();
        }

        public void save(com.github.tommwq.applogmanagement.AppLogManagementProto.Command cmd) {
                new Call(() -> helper.insert(new CommandRepositoryApi.Command(cmd))).rethrow();
        }
        
        public void remove(com.github.tommwq.applogmanagement.AppLogManagementProto.Command cmd) {
                new Call(() -> helper.delete(new CommandRepositoryApi.Command(cmd)));
        }

        public List<CommandRepositoryApi.Command> loadAllEntity() {
                return (List<CommandRepositoryApi.Command>) new Call((Void) -> helper.select(CommandRepositoryApi.Command.class), null, null).rethrow().result();                
        }

        public List<com.github.tommwq.applogmanagement.AppLogManagementProto.Command> loadAll() {
                return loadAllEntity().stream()
                        .map(cmd -> (com.github.tommwq.applogmanagement.AppLogManagementProto.Command) new Call((Void)->{
                                                com.github.tommwq.applogmanagement.AppLogManagementProto.Command.Builder builder = com.github.tommwq.applogmanagement.AppLogManagementProto.Command.newBuilder();
                                                TextFormat.getParser().merge(cmd.command, builder);
                                                return builder.build();
                        }, null, null).rethrow().result())
                        .filter(Predicates::notNull)
                        .collect(Collectors.toList());
        }
}
