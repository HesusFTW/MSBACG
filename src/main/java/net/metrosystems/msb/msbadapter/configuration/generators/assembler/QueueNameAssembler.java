package net.metrosystems.msb.msbadapter.configuration.generators.assembler;

import java.util.Iterator;
import java.util.List;

/**
 * Assembles Queue names.
 *
 * @author benjamin.stein
 */
public class QueueNameAssembler {

	private static final String SEPARATOR = ".";
	private static final String QUEUE_PREFIX = "msb" + SEPARATOR;
	private static final String QUEUES_SEPARATOR = "#";
	private static final String QUEUES_INTERFACE = "all";
	private static final String TRANSFER_DIRECTION = "upload";
	private static final String QUEUES_SUFFIX = "delivery";
	private static final String SPECIAL_QUEUE = "mip";
	private static Iterator<String> iterator;

	/**
	 * This Method assembles the QueueNames used for Events. It implements the
	 * Queue naming convention decided for MSBAdapter used on Stores
	 *
	 * @param nodeId    nodeId
	 * @param eventName eventName
	 * @return the generated Queue Name
	 */
	public static String assembleShortQueue(String nodeId, String eventName) {
		StringBuilder sb = new StringBuilder();
		eventName = eventName.trim().toLowerCase();
		if (!eventName.startsWith(SPECIAL_QUEUE)) {
			sb.append(QUEUE_PREFIX);
			if (!eventName.equalsIgnoreCase(TRANSFER_DIRECTION))
				sb.append(nodeId);
			sb.append(SEPARATOR);
		}
		sb.append(eventName);
		return sb.toString();
	}

	public static String assembleMultiQueue(String nodeId, List<String> queueNames) {
		iterator = queueNames.iterator();
		StringBuilder sb = new StringBuilder();
		String queueName;
		while (iterator.hasNext()) {
			queueName = iterator.next().toLowerCase().trim();

			if (queueName.startsWith(SPECIAL_QUEUE)) {
				sb.append(queueName);
			} else {
				sb.append(QUEUE_PREFIX);
				if (!queueName.equalsIgnoreCase(TRANSFER_DIRECTION)) {
					sb.append(nodeId);
					sb.append(SEPARATOR);
				}
				sb.append(queueName);
			}

			if (iterator.hasNext()) {
				sb.append(QUEUES_SEPARATOR);
			}
		}
		return sb.toString();
	}

	public static String assembleMultiQueue(List<String> queueNames) {
		iterator = queueNames.iterator();
		StringBuilder sb = new StringBuilder();
		String queueName;
		while (iterator.hasNext()) {
			queueName = iterator.next().toLowerCase().trim();
			if (!queueName.isEmpty()) {
				if (sb.length() != 0) {
					sb.append(QUEUES_SEPARATOR);
				}
				sb.append(queueName);
			}
		}
		return sb.toString();
	}

	public static String assembleDetailQueue(String nodeId, String salesline, String source) {
		StringBuilder sb = new StringBuilder();
		sb.append(QUEUE_PREFIX);
		sb.append(salesline.toLowerCase());
		sb.append(SEPARATOR);
		sb.append(nodeId.substring(5));
		sb.append(SEPARATOR);
		sb.append(source.toLowerCase());
		sb.append(SEPARATOR);
		sb.append(QUEUES_INTERFACE);
		sb.append(SEPARATOR);
		sb.append(QUEUES_SUFFIX);
		return sb.toString();
	}
}
