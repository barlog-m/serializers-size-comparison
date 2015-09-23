package com.example;

import com.example.model.thrift.Data;
import com.example.model.thrift.Version;
import com.example.model.thrift.Message;
import com.example.model.thrift.MessageType;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public class ThriftExample {
	private static final Data data = new Data();

	static {
		Version version = new Version();

		data.setTimestamp(Instant.now().getEpochSecond());
		data.setVersion(version);

		data.setMessages(new ArrayList<Message>(1000));
	}

	public static void addData(String header, String value) {
		Message m = new Message();
		m.setType(MessageType.DATA);
		m.setHeader(header);
		m.setValue(value);

		data.getMessages().add(m);
	}

	public static void calc() {
		TSerializer serializer = new TSerializer(new TCompactProtocol.Factory());
		byte[] bytes = null;
		try {
			bytes = serializer.serialize(data);
			System.out.println("thrift unpacked: " + bytes.length);
		} catch (TException e) {
			e.printStackTrace();
		}

		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream, new Deflater());
			deflaterOutputStream.write(bytes, 0, bytes.length);
			deflaterOutputStream.close();
			System.out.println("thrift deflated: " + byteArrayOutputStream.toByteArray().length);
		} catch (Exception e) {
			e.printStackTrace();
		}

/*
		TDeserializer deserializer = new TDeserializer(new TBinaryProtocol.Factory());
		Data moreWork = new Data();
		deserializer.deserialize(moreWork, bytes);
*/
	}
}
