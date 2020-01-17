package itx.examples.springbank.server.service;

import itx.examples.springbank.common.dto.LedgerRecord;
import itx.examples.springbank.common.dto.ServiceException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class LedgerServiceImpl implements LedgerService {

    @Override
    public void saveRecord(LedgerRecord ledgerRecord) throws ServiceException {

    }

    @Override
    public Collection<LedgerRecord> getLedger() {
        return null;
    }

}
