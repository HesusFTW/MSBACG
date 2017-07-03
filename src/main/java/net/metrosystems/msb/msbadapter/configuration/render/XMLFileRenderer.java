package net.metrosystems.msb.msbadapter.configuration.render;

import net.metrosystems.msb.msbadapter.configuration.data.ConfigMapItem;

import java.util.Map;

/**
 * Provides Methods to Render XML files
 *
 * @author benjamin.stein
 */
public interface XMLFileRenderer {

	/**
	 * /** starts the rendering of EventConfigs Document.
	 *
	 * @param outputDirPath  - the path to the output dir
	 * @param configurations - a Map with ConfigMapItems
	 * @throws RendererException if eventConfig can't be rendered
	 */
	public void renderEventConfigs(String outputDirPath,
								   Map<String, ConfigMapItem> configurations) throws RendererException;

	/**
	 * /** starts the rendering of AdminConfigs Document.
	 *
	 * @param outputDirPath  - the path to the output dir
	 * @param configurations - a Map with ConfigMapItems
	 * @throws RendererException if adminConfig can't be rendered
	 */
	public void renderAdminConfigs(String outputDirPath,
								   Map<String, ConfigMapItem> configurations) throws RendererException;
}
