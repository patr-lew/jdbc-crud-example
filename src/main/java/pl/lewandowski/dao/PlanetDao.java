package pl.lewandowski.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.lewandowski.DbUtil;
import pl.lewandowski.model.Planet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class PlanetDao {
    private static final String CREATE_PLANET_QUERY
            = "INSERT INTO solar_system.planet(`name`, `biggest_moon`, `solar_day`, `distance_sun`) VALUES (?,?,?,?)";
    private static final String READ_PLANET_ON_ID_QUERY
            = "SELECT `name`, `biggest_moon`, `solar_day`, `distance_sun` FROM solar_system.planet WHERE id = ?";
    private static final String UPDATE_PLANET_QUERY
            = "UPDATE solar_system.planet SET `name` = ?, `biggest_moon` = ?, `solar_day` = ?, `distance_sun` = ? WHERE `id` = ?";
    private static final String DELETE_PLANET_QUERY = "DELETE FROM solar_system.planet WHERE `id` = ?";

    private static final Logger log = LoggerFactory.getLogger(PlanetDao.class);
    private final DbUtil dbUtil;

    public PlanetDao(DbUtil dbUtil) {
        this.dbUtil = dbUtil;
    }

    public void create(Planet planet) {
        try (Connection connection = dbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_PLANET_QUERY)) {

            statement.setString(1, planet.getName());
            statement.setString(2, planet.getBiggestMoon());
            statement.setDouble(3, planet.getSolarDay());
            statement.setLong(4, planet.getDistanceSun());

            statement.executeUpdate();

            ResultSet result = statement.getGeneratedKeys();
            if (result.next()) {
                Long id = result.getLong(1);
                planet.setId(id);
                log.debug("Planet {} inserted into database with id = {}", planet.getName(), id);
            } else {
                throw new SQLException("Adding planet to the database unsuccessful");
            }

        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    public Optional<Planet> read(long id) {
        try (Connection connection = dbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(READ_PLANET_ON_ID_QUERY)) {
            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                Planet planet = new Planet();

                planet.setId(id);
                planet.setName(result.getString("name"));
                planet.setBiggestMoon(result.getString("biggest_moon"));
                planet.setSolarDay(result.getDouble("solar_day"));
                planet.setDistanceSun(result.getLong("distance_sun"));

                log.debug("Planet {} with id = {} read from the solar_system database", planet.getName(), id);
                return Optional.of(planet);
            } else {
                log.info("Planet with id = {} doesn't exist in the solar_system database", id);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return Optional.empty();
    }

    public void update(Planet planet) {
        try (Connection connection = dbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_PLANET_QUERY)) {
            statement.setString(1, planet.getName());
            statement.setString(2, planet.getBiggestMoon());
            statement.setDouble(3, planet.getSolarDay());
            statement.setLong(4, planet.getDistanceSun());
            statement.setLong(5, planet.getId());

            int updated = statement.executeUpdate();

            if (updated > 0) {
                log.info("Successfully updated planet {}, id = {}", planet.getName(), planet.getId());
            } else {
                log.error("Update of planet {}, id = {} unsuccessful", planet.getName(), planet.getId());
            }

        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    public void delete(long id) {
        if (read(id).equals(Optional.empty())) {
            log.info("Planet under id {} doesn't exist. Deletion unsuccessful.", id);
            return;
        }

        try (Connection connection = dbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_PLANET_QUERY)) {
            statement.setLong(1, id);
            statement.executeUpdate();

            log.info("Planet under id {} deleted from the solar_system database", id);

        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }
}
