package itx.examples.redis.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import itx.examples.redis.dto.UserData;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class DataServiceImpl implements DataService {

    private static final String OK = "OK";

    private final Jedis jedis;
    private final ObjectMapper mapper;

    public DataServiceImpl(String host, int port) {
        this.jedis = new Jedis(host, port);
        this.mapper = new ObjectMapper();
    }

    @Override
    public Optional<UserData> findById(String id) throws DataServiceException {
        try {
            String data = this.jedis.get(createKey(id));
            if (data != null) {
                return Optional.of(mapper.readValue(data, UserData.class));
            } else {
                return Optional.empty();
            }
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public UserData save(String email, String password) throws DataServiceException {
        try {
            UserData userData = new UserData(UUID.randomUUID().toString(), email, password);
            String reply = this.jedis.set(createKey(userData), mapper.writeValueAsString(userData));
            if (OK.equals(reply)) {
                return userData;
            } else {
                throw new DataServiceException("Redis write has failed !");
            }
        } catch (IOException e) {
            throw new DataServiceException(e);
        }
    }

    @Override
    public Collection<UserData> findAll() throws DataServiceException {
        List<UserData> result = new ArrayList<>();
        Set<String> keys = this.jedis.keys(UserData.class.getCanonicalName() + ":*");
        for (String key: keys) {
            try {
                result.add(mapper.readValue(jedis.get(key), UserData.class));
            } catch (JsonProcessingException e) {
            }
        }
        return result;
    }

    @Override
    public void deleteById(String id) throws DataServiceException {
        this.jedis.del(createKey(id));
    }

    @Override
    public void update(UserData userData) throws DataServiceException {
        try {
            this.jedis.set(createKey(userData), mapper.writeValueAsString(userData));
        } catch (IOException e) {
            throw new DataServiceException(e);
        }
    }

    private static String createKey(UserData userData) {
        return UserData.class.getCanonicalName() + ":" + userData.getId();
    }

    private static String createKey(String id) {
        return UserData.class.getCanonicalName() + ":" + id;
    }

}
