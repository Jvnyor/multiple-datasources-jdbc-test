package com.jvnyor.multdatasourcesjdbc.infra.database.dao.topic;

import com.jvnyor.multdatasourcesjdbc.model.topic.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class TopicDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(TopicDAO.class);

    private static final String INSERT_TOPIC = "INSERT INTO topics (name) VALUES (?)";

    private static final String SELECT_ALL_TOPICS = "SELECT * FROM topics";

    private static final String SELECT_TOPIC_BY_ID = "SELECT * FROM topics WHERE id = ?";

    private static final String UPDATE_TOPIC = "UPDATE topics SET name = ? WHERE id = ?";

    @Qualifier("topicsDataSource")
    private final DataSource topicsDataSource;

    public TopicDAO(DataSource topicsDataSource) {
        this.topicsDataSource = topicsDataSource;
    }

    public void save(Topic topic) {
        LOGGER.info("Saving topic");

        try (Connection conn = topicsDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_TOPIC)) {
            ps.setString(1, topic.name());
            int executedUpdate = ps.executeUpdate();

            LOGGER.info("Topic saved, rows affected: {}", executedUpdate);
        } catch (Exception e) {
            LOGGER.error("Error saving topic", e);
        }

        LOGGER.info("Topic saved");
    }

    public void update(Topic topic) {
        LOGGER.info("Updating topic");

        try (Connection conn = topicsDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_TOPIC)) {
            ps.setString(1, topic.name());
            ps.setLong(2, topic.id());

            int executedUpdate = ps.executeUpdate();

            LOGGER.info("Topic updated, rows affected: {}", executedUpdate);
        } catch (Exception e) {
            LOGGER.error("Error updating topic", e);
        }

        LOGGER.info("Topic updated");
    }

    public List<Topic> findAll() {
        LOGGER.info("Finding all topics");

        var topics = new ArrayList<Topic>();

        try (Connection conn = topicsDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_TOPICS);
             var rs = ps.executeQuery()) {

            while (rs.next()) {
                topics.add(new Topic(rs.getLong("id"), rs.getString("name")));
            }
        } catch (Exception e) {
            LOGGER.error("Error finding all topics", e);
        }

        LOGGER.info("All topics found");
        return topics;
    }

    public Optional<Topic> findById(Long id) {
        LOGGER.info("Finding topic by id");

        try (Connection conn = topicsDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_TOPIC_BY_ID)) {
            ps.setLong(1, id);

            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Topic(rs.getLong("id"), rs.getString("name")));
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error finding topic by id", e);
        }

        LOGGER.info("Topic found by id");
        return Optional.empty();
    }
}
