/*********************************************************************************************
 *
 * 'GamaListConverter.java, in plugin ummisco.gama.serialize, is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package ummisco.gama.serializer.gamaType.converters;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import msi.gama.util.GamaList;
import ummisco.gama.serializer.gamaType.reduced.GamaListReducer;
import ummisco.gama.serializer.gamaType.reduced.GamaListReducerNetwork;

@SuppressWarnings({ "rawtypes" })
public class GamaListConverterNetwork implements Converter {

	ConverterScope convertScope;

	public GamaListConverterNetwork(final ConverterScope s) {
		convertScope = s;
	}

	@Override
	public boolean canConvert(final Class arg0) {
		if (GamaList.class.equals(arg0)) {
			return true;
		}

		final Class<?>[] allInterface = arg0.getInterfaces();
		for (final Class<?> c : allInterface) {
			if (c.equals(GamaList.class))
				return true;
		}
		return false;
	}

	@Override
	public void marshal(final Object arg0, final HierarchicalStreamWriter writer, final MarshallingContext arg2) {
		final GamaList list = (GamaList) arg0;

		System.out.println("ConvertAnother : GamaList " + list.getClass()+ " "+list.getType().getContentType());
		arg2.convertAnother(new GamaListReducerNetwork(list));
		System.out.println("END --- ConvertAnother : GamaList ");

	}

	@Override
	public Object unmarshal(final HierarchicalStreamReader reader, final UnmarshallingContext arg1) {
		// reader.moveDown();
		final GamaListReducerNetwork rmt = (GamaListReducerNetwork) arg1.convertAnother(null, GamaListReducerNetwork.class);
		// reader.moveUp();
		return rmt.constructObject(convertScope.getScope());
	}

}
