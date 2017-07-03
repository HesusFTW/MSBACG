package net.metrosystems.msb.msbadapter.configuration.generators.assembler;

import java.util.Iterator;
import java.util.List;

public class McdBoTypeAssembler {
	private static final String SEPARATOR = "_";
	private static final String DATA_TAG = "DATA";
        private static Iterator<String> iterator;


        public static String assemble(List<String> queueNames){
            StringBuilder sb = new StringBuilder();
             iterator = queueNames.iterator();
            while (iterator.hasNext()){
                sb.append(iterator.next());
                sb.append(SEPARATOR);
            }
             sb.append(DATA_TAG);
            return sb.toString();
        }

}
