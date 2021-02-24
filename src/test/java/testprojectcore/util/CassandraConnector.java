package testprojectcore.util;

import java.util.ArrayList;
import java.util.List;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;


public class CassandraConnector {

    public static Session getSession() {
        String serverIP = "";
        String keyspace = "";
        CassandraConnector cassandraConnector;
        Cluster cluster = Cluster.builder()
                .addContactPoint(serverIP)
                .withCredentials("", "")
                .withPort(9042)
                .build();
        Session session = cluster.connect(keyspace);
        return session;
    }

    public static List<String> executeQuery(String query) {
        String cqlStatement = query;
        Session cassandraSession = getSession();
        List<String> result = new ArrayList<>();
        for (Row row : cassandraSession.execute(cqlStatement)) {
            result.add(row.toString());
        }
        return result;
    }

}
