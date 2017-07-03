package net.metrosystems.msb.msbadapter.configuration.generators.assembler;

import java.util.Iterator;
import java.util.List;

/**
 * Assembles the Name of the target folders or target queues
 * @author georgiana.zota
 *
 */
public class TargetsListAssembler {
	
	private static final String FOLDER_SEPARATOR = "#";
        private static Iterator<String> iterator;
	

        public static String assemble(List<String> queueNames) {
            iterator = queueNames.iterator();
            StringBuilder sb = new StringBuilder();
            while (iterator.hasNext()) {
            	sb.append(iterator.next().toString());         
                if (iterator.hasNext()) {
                    sb.append(FOLDER_SEPARATOR);
                }
            }
            return sb.toString();
        }

}
