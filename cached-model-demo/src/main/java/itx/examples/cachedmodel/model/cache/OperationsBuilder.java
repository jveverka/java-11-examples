package itx.examples.cachedmodel.model.cache;

import itx.examples.cachedmodel.model.keys.CK;

import java.util.ArrayList;
import java.util.Collection;

public class OperationsBuilder {

    private final Collection<Operation> operations;

    public OperationsBuilder() {
        this.operations = new ArrayList<>();
    }

    public OperationsBuilder addOperation(OperationType type, CK ck, Object object) {
        operations.add(new Operation(type, ck, object));
        return this;
    }

    public OperationsBuilder addWriteOperation(CK ck, Object object) {
        operations.add(new Operation(OperationType.WRITE, ck, object));
        return this;
    }

    public OperationsBuilder addDeleteOperation(CK ck) {
        operations.add(new Operation(OperationType.DELETE, ck, null));
        return this;
    }

    public Collection<Operation> build() {
        return operations;
    }

    public static OperationsBuilder builder() {
        return new OperationsBuilder();
    }

    public class Operation {
        private final OperationType type;
        private final CK ck;
        private final Object object;

        public Operation(OperationType type, CK ck, Object object) {
            this.type = type;
            this.ck = ck;
            this.object = object;
        }

        public OperationType getType() {
            return type;
        }

        public CK getCk() {
            return ck;
        }

        public Object getObject() {
            return object;
        }
    }

}
