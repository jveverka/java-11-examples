package itx.examples.jdbc;

import itx.examples.jdbc.dto.DataRecord;
import itx.examples.jdbc.dto.DataRecordProcessor;

import java.util.Collection;

public interface TimeSeriesService {

    void registerDataRecordProcessor(DataRecordProcessor<?> dataRecordProcessor);

    void unregisterDataRecordProcessor(Class<?> type);

    void saveRecord(DataRecord<?> dataRecord);

    <T> Collection<DataRecord<T>> getDataRecords(Class<T> type, long beginTimeStamp, long endTimeStamp);

    <T> Collection<DataRecord<T>> getDataRecords(Class<T> type, long beginTimeStamp);

    <T> void deleteDataRecords(Class<T> type, long endTimeStamp);

    <T> void deleteSeries(Class<T> type);

}
