package com.github.tommwq.applogmanagement.repository;

import com.github.tommwq.applogmanagement.AppLogManagementProto.Command;
import com.github.tommwq.utility.database.DBHelper;
import com.github.tommwq.utility.database.SQLiteHelper;
import com.github.tommwq.utility.function.Predicates;
import com.github.tommwq.utility.Util;
import com.google.protobuf.TextFormat;
import com.google.protobuf.TextFormat.ParseException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SQLiteCommandRepository implements CommandRepository {

        private static class CommandEntity {
                String command;

                public CommandEntity(String aCommand) {
                        command = aCommand;
                }
        }

        private Connection conn;
        private DBHelper helper;
                
        public SQLiteCommandRepository(File databaseFile) throws SQLException {
                conn = DriverManager.getConnection("jdbc:sqlite:" + databaseFile.getName());
                helper = new SQLiteHelper(conn).createTableInNeed(CommandEntity.class);
        }
        
        @Override
        public void save(Command aCommand) {
                try {
                        save(new CommandEntity(aCommand.toString()));
                } catch (SQLException e) {
                        throw new RuntimeException("fail to save command entity", e);
                }
        }

        private void save(CommandEntity command) throws SQLException {
                helper.insert(command);
        }

        @Override
        public List<Command> loadAll() {
                try {
                        return loadCommandEntity()
                                .stream()
                                .map(cmd -> {
                                                try {
                                                        Command.Builder builder = Command.newBuilder();
                                                        com.google.protobuf.TextFormat.getParser().merge(cmd.command, builder);
                                                        return builder.build();
                                                } catch (ParseException e) {
                                                        return null;
                                                }
                                        })
                                .filter(Predicates::notNull)
                                .collect(Collectors.toList());
                } catch (SQLException e) {
                        throw new RuntimeException("fail to load command entity", e);
                }
        }
  
        public List<CommandEntity> loadCommandEntity() throws SQLException {
                List<CommandEntity> cmdList = new ArrayList<>();
                String query = helper.selectSQL(CommandEntity.class);
                try (PreparedStatement pstmt = conn.prepareStatement(query);
                     ResultSet resultSet = pstmt.executeQuery()) {
                                while (resultSet.next()) {
                                        String cmd = resultSet.getString(1);
                                        cmdList.add(new CommandEntity(cmd));
                                }

                                return cmdList;
                }
        }

        @Override
        public void remove(Command command) {

        }
}
