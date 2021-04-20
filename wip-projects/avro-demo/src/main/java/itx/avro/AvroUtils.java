package itx.avro;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class AvroUtils {

    private AvroUtils() {
    }

    public static byte[] serializeEmployee(Employee employee) throws IOException {
        DatumWriter<Employee> employeeDatumWriter = new SpecificDatumWriter<>(Employee.class);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        BinaryEncoder binaryEncoder = EncoderFactory.get().directBinaryEncoder(os, null);
        employeeDatumWriter.setSchema(Employee.getClassSchema());
        employeeDatumWriter.write(employee, binaryEncoder);
        os.flush();
        return os.toByteArray();
    }

    public static Employee deserializeEmployee(byte[] data) throws IOException {
        DatumReader<Employee> employeeDatumReader = new SpecificDatumReader<>(Employee.class);
        BinaryDecoder binaryDecoder = DecoderFactory.get().binaryDecoder(data, null);
        employeeDatumReader.setSchema(Employee.getClassSchema());
        return employeeDatumReader.read(null, binaryDecoder);
    }

}
