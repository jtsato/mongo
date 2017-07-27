package io.github.jtsato.util;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;

import com.mongodb.MongoWriteException;

/**
 * Created by Jorge Takeshi Sato on 25/07/2017.
 */

public class MasterUtil {

	public static Document convert(String string) {
		if (StringUtils.isBlank(string)) {
			return new Document();
		}
		final String stringDecoded = Base64.isBase64(string) ? new String(Base64.decodeBase64(string)) : string;
		return Document.parse(stringDecoded);
	}

	public static String convert(Document document) {
		if (document == null) {
			return StringUtils.EMPTY;
		}
		return new String(Base64.encodeBase64(document.toJson().getBytes()));
	}

	public static String buildSuccess() {
		return MasterUtil.buildSuccessOne(null);
	}

	public static String buildSuccessMany(Collection<Document> documents) {
		final StringBuilder stringBuilder = new StringBuilder();
		for (Iterator<Document> iterator = documents.iterator(); iterator.hasNext();) {
			final Document document = (Document) iterator.next();
			stringBuilder.append(document.toJson());
		}
		final String string = String.format("[%s]", StringUtils.stripToEmpty(stringBuilder.toString()));
		return String.format("{ 'status': 'SUCCESS', 'result': '%s' }", new String(Base64.encodeBase64(string.getBytes())));
	}

	public static String buildSuccessOne(Document document) {
		return String.format("{ 'status': 'SUCCESS', 'result': '%s' }", document != null ? convert(document) : StringUtils.EMPTY);
	}

	public static String buildError(Exception exception) {
		if (exception instanceof MongoWriteException) {
			return String.format("{ 'status': 'ERROR', 'result': '%s' }", StringUtils.substringBefore(exception.getMessage(), ":"));
		}
		return String.format("{ 'status': 'ERROR', 'result': '%s' }", exception.getMessage(), ":");
	}
}
