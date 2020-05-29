package itx.examples.cachedmodel.model.keys;

import java.util.Arrays;
import java.util.Objects;

/**
 * Composed Key
 */
public final class CK<T> {

    private final Class<T> type;
    private final ID[] ids;

    public CK(Class<T> type, ID... ids) {
        this.type = type;
        this.ids = ids;
    }

    public CK(Class<T> type, ID id) {
        this.type = type;
        this.ids = new ID[] { id };
    }

    public CK(Class<T> type, String id) {
        this.type = type;
        this.ids = new ID[] { new ID(id) {}};
    }

    public Class<T> getType() {
        return type;
    }

    public ID[] getIds() {
        return ids;
    }

    public boolean startsWith(CK ck) {
        if (ids.length < ck.getIds().length) {
            return false;
        }
        for(int i=0; i<ck.getIds().length; i++) {
            if (!ids[i].equals(ck.getIds()[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CK ck = (CK) o;
        return Objects.equals(type, ck.type) &&
                Arrays.equals(ids, ck.ids);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(type);
        result = 31 * result + Arrays.hashCode(ids);
        return result;
    }

    public static <T>  CK<T> from(Class<T> type, ID ... ids) {
        return new CK(type, ids);
    }

    @Override
    public String toString() {
        return type.getName() + ":" + Arrays.toString(ids);
    }
}
