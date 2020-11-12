package itx.examples.mongodb.services;

import com.mongodb.client.MongoCursor;
import itx.examples.mongodb.dto.Address;
import org.bson.Document;
import org.mongojack.JacksonMongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;

import static com.mongodb.client.model.Filters.eq;

public class AddressServiceImpl implements AddressService {

    private static final Logger LOG = LoggerFactory.getLogger(AddressServiceImpl.class);

    private final JacksonMongoCollection<Address> collection;

    public AddressServiceImpl(JacksonMongoCollection<Address> collection) {
        this.collection = collection;
    }

    @Override
    public Collection<Address> getAll() {
        LOG.info("get all");
        Collection<Address> roles = new ArrayList<>();
        MongoCursor<Address> rolesIterator = collection.getMongoCollection().find().iterator();
        while (rolesIterator.hasNext()) {
            roles.add(rolesIterator.next());
        }
        return roles;
    }

    @Override
    public void insert(Address address) throws DataException {
        LOG.info("insert address: {} {} {}", address.getId(), address.getStreet(), address.getCity());
        collection.insert(address);
    }

    @Override
    public void update(Address address) throws DataException {
        LOG.info("update address: {} {} {}", address.getId(), address.getStreet(), address.getCity());
        Document addressDoc = new Document();
        addressDoc.put("street", address.getStreet());
        addressDoc.put("city", address.getCity());
        Document updateObject = new Document();
        updateObject.put("$set", addressDoc);
        collection.updateOne(eq("_id", address.getId()), updateObject);
    }

    @Override
    public Address get(String id) throws DataException {
        LOG.info("get address: {}", id);
        return collection.findOneById(id);
    }

    @Override
    public void remove(String id) throws DataException {
        LOG.info("remove address: {}", id);
        collection.removeById(id);
    }

    @Override
    public void removeAll() throws DataException {
        LOG.info("remove all");
        collection.drop();
    }

}
