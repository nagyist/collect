Collect.DataManager.MapPanelComposer = function(panel) {
	var $this = this;

	this.$panel = panel;
	this.map = null;
	this.initialized = false;
	this.dependenciesLoaded = false;
	
	this.verticalPadding = 220;
	this.horizontalPadding = 50;
	
	this.startLat = 30;
	this.startLon = 0;
	this.startZoom = 4;
}

Collect.DataManager.MapPanelComposer.prototype.init = function(onComplete) {
	var $this = this;
	
	$this.resizeMapContainer();
	
	this.popupContainer = $(
		'<div class="ol-popup">' +
			'<a href="#" class="ol-popup-closer"></a>' +
			'<div class="popup-content"></div>' +
		'</div>'
	);

	this.$panel.append(this.popupContainer);

	this.popupContent = this.popupContainer.find('.popup-content');
	
	/**
     * Create an overlay to anchor the popup to the map.
     */
	this.overlay = new ol.Overlay( /** @type {olx.OverlayOptions} */ ({
		element : $this.popupContainer[0],
		autoPan : true,
		autoPanAnimation : {
			duration : 250
		}
	}));

	/**
	 * Add a click handler to hide the popup.
	 * @return {boolean} Don't follow the href.
	 */
	var popupCloser = this.popupContainer.find('.ol-popup-closer');
	popupCloser.click(function() {
		$this.overlay.setPosition(undefined);
		popupCloser.blur();
		return false;
	});
	
	if ($this.dependenciesLoaded) {
		$this.onDependenciesLoaded(onComplete);
	} else {
		System.import('openlayers').then(function() {
			$this.dependenciesLoaded = true;
			$this.onDependenciesLoaded(onComplete);
		});
	}
}

Collect.DataManager.MapPanelComposer.prototype.onDependenciesLoaded = function(onComplete) {
	var $this = this;

	$(window).resize(function() {
		$this.resizeMapContainer();
	});

	var surveysOverlayGroup = new ol.layer.Group({
		title : 'Surveys',
		layers : []
	});

	$this.map = new ol.Map({
		target : 'map',
		layers : [
			$this.createBaseMapsLayer(),
			surveysOverlayGroup
		],
		view : new ol.View({
			projection: 'EPSG:4326',
			center : [$this.startLon, $this.startLat],
			zoom : $this.startZoom
		}),
		overlays : [ $this.overlay ],
	});

	var layerSwitcher = new ol.control.LayerSwitcher({
		tipLabel : 'Layers' // Optional label for button
	});
	$this.map.addControl(layerSwitcher);

	var displayFeatureInfo = function(pixel, coordinate) {
		var feature = $this.map.forEachFeatureAtPixel(pixel, function(feature) {
			return feature;
		});
		if (feature) {
			var survey = feature.get('survey');
			var htmlContent;
			
			switch (feature.get('type')) {
			case 'sampling_point':
				var lonLat = coordinate;
				var keyDefs = survey.getRooEntityKeyDefinitions();
				function printLevelCodes(levelCodes) {
					var result = "";
					for (var i = 0; i < levelCodes.length; i++) {
						var keyDef = keyDefs.length > i ? keyDefs[i] : null;
						var levelName = keyDef ? keyDef.getLabelOrName() : "level " + (i + 1);
						result += levelName + ": " + levelCodes[i] + "<br>";
					}
					return result;
				}
				var levelCodes = feature.get('name').split('|');
				htmlContent = OF.Strings.format(
						//TODO improve level codes formatting
						"<b>Sampling Point</b>"
						+ "<br>"
						+ "{0}"
						+ "Latitude: {1}"
						+ "<br>"
						+ "Longitude: {2}"
						+ "<br>"
						, printLevelCodes(levelCodes), lonLat[1], lonLat[0]);
				break;
			case 'coordinate_attribute_value':
				var point = feature.get('point');
				htmlContent = $this.createNodeInfoBalloon(survey, point);
				break;
			}
			$this.popupContent.html(htmlContent);
			$this.popupContent.find(".accordion").accordion({heightStyle: "content", animate: 0});
			$this.popupContent.find(".data.info-icon-button").tooltip({
				html: true,
				title: 'In order to show or hide an attribute, use the \"Show in Map balloon\" option in the Survey Designer'
			});
			$this.overlay.setPosition(coordinate);
		}
	};

	$this.map.on('singleclick', function(evt) {
		displayFeatureInfo(evt.pixel, evt.coordinate);
	});

	collect.surveyService.loadFullPublishedSurveys(function(surveys) {
		surveys.forEach(function(jsonSurvey) {
			var survey = new Collect.Metamodel.Survey(jsonSurvey);
			var surveyGroup = $this.createSurveyLayerGroup(survey);
			surveysOverlayGroup.getLayers().push(surveyGroup);
		});
	}, function() {}, true);

	$this.initialized = true;

	if (onComplete) {
		onComplete();
	}
}

Collect.DataManager.MapPanelComposer.prototype.createNodeInfoBalloon = function(survey, nodeInfo) {
	var lonLat = [nodeInfo.x, nodeInfo.y];
	
	var dynamicPart = "";
	var data = nodeInfo.recordData;
	data.forEach(function(item) {
		var def = survey.getDefinition(item.definitionId);
		dynamicPart += "<label>" + def.label + "</label>: " + (item.value == null ? "-": item.value);
		dynamicPart += "<br/>";
	});

	var result = OF.Strings.format(
		"<b>{0}</b>"
		+ "<br>"
		+ "<label>Record</label>: {1}"
		+ "<br>"
		+ "<label>Phase</label>: {2}"
		+ "<br>"
		+ "<div class='accordion' style='width: 300px; height: 200px; padding: 0.5em'>"
		+ "   <h3>"
		+ "     <span>Data</span>"
		+ "     <span class='data info-icon-button' style='float: right;' />"
		+ "   </h3>"
		+ "   <div style='min-height: 135px; max-height: 135px; overflow-y: auto; padding: 0.5em'>"
		+ "      <p>{3}</p>"
		+ "   </div>"
		+ "   <h3>Location</h3>"
		+ "   <div style='min-height: 135px; padding: 0.5em'>"
		+ "      <p>"
		+ "         <label>Latitude:</label> {4}" 
		+ "         <br>"
		+ "         <label>Longitude</label>: {5}"
		+ "         <br>" 
		+ "         {6}"
		+ "      </p>"
		+ "   </div>"
		+ "</div>"
		+ "<br>" 
		+ "<a href=\"javascript:void(0);\" "
		+ "onclick=\"Collect.DataManager.MapPanelComposer.openRecordEditPopUp({7}, {8}, '{9}')\">Edit Record</a>"
		+ "</div>"
	, survey.getDefinition(nodeInfo.attrDefId).label
	, nodeInfo.recKeys
	, nodeInfo.recStep
	, dynamicPart
	, lonLat[1]
	, lonLat[0]
	, (isNaN(nodeInfo.distance) ? "" : "<label>Dist. to expected loc.</label>: " +
			Math.round(nodeInfo.distance) + "m")
	, survey.id
	, nodeInfo.recId
	, nodeInfo.recKeys);
	
	return result;
};

Collect.DataManager.MapPanelComposer.prototype.createSurveyLayerGroup = function(survey) {
	var $this = this;
	
	var dataLayers = new Array();
	survey.traverse(function(nodeDef) {
		if (nodeDef.type == 'ATTRIBUTE' && nodeDef.attributeType == 'COORDINATE') {
			var dataLayer = new ol.layer.Vector({
				title : OF.Strings.firstNotBlank(nodeDef.label, nodeDef.name),
				visible : false,
				type : 'coordinate_data',
				survey : survey,
				coordinate_attribute_def : nodeDef,
				source : null,
				style : $this.coordinateAttributeLayerStyleFunction
			});
			dataLayers.push(dataLayer);
		} else if (nodeDef.type == 'ATTRIBUTE' && nodeDef.geometry) {
			var dataLayer = new ol.layer.Vector({
				title : OF.Strings.firstNotBlank(nodeDef.label, nodeDef.name),
				visible : false,
				type : 'geometry_data',
				survey : survey,
				attribute_def : nodeDef,
				source : null,
				style : $this.geometryLayerStyleFunction
			});
			dataLayers.push(dataLayer);
		}
	});

	var surveyGroup = new ol.layer.Group({
		title : OF.Strings.firstNotBlank(survey.projectName, survey.name),
		layers : [
			new ol.layer.Group({
				title : 'Data',
				layers : dataLayers
			}),
			new ol.layer.Vector({
				title : 'Sampling Points',
				visible : false,
				type : 'sampling_points',
				survey : survey,
				style : new ol.style.Style({
					image : new ol.style.Circle({
						fill : new ol.style.Fill({
							color : [0,0,0,0] //transparent fill
						}),
						stroke: new ol.style.Stroke({
							color : "#0000FF",
							width: 2
						}),
						radius : 5
					})
				})
			})
		]
	});

	function bindLayerEventListeners(layer) {
		layer.on('change:visible', $.proxy($this.onTileVisibleChange, $this));
	}

	surveyGroup.getLayers().forEach(function(layer) {
		bindLayerEventListeners(layer);
	});
	dataLayers.forEach(function(layer) {
		bindLayerEventListeners(layer);
	});

	return surveyGroup;
};

Collect.DataManager.MapPanelComposer.prototype.coordinateAttributeLayerStyleFunction = function(feature) {
	var point = feature.get('point');
	var step = point.recStep;
	var color;
	switch (step) {
	case 'ENTRY':
		color = "#FF0000";
		break;
	case 'CLEANSING':
		color = "#FF9933";
		break;
	case 'ANALYSIS':
		color = "#00FF00";
		break;
	}
	var style = new ol.style.Style({
		image : new ol.style.Circle({
			fill : new ol.style.Fill({
				color : color
			}),
			radius : 5
		})
	});
	return [style];
};

Collect.DataManager.MapPanelComposer.prototype.geometryLayerStyleFunction = function(feature) {
	var $this = this;
	
	var survey = feature.get("survey");
	var lineColor = $this.stringToColor(survey.name);
	
	var styles = [
		/* We are using two different styles for the polygons:
		 *  - The first style is for the polygons themselves.
		 *  - The second style is to draw the vertices of the polygons.
		 *    In a custom `geometry` function the vertices of a polygon are
		 *    returned as `MultiPoint` geometry, which will be used to render
		 *    the style.
		 */
		new ol.style.Style({
			stroke : new ol.style.Stroke({
				color : lineColor,
				width : 3
			}),
			fill : new ol.style.Fill({
				color : [0,0,0,0]
			})
		}),
		new ol.style.Style({
			image : new ol.style.Circle({
				radius : 5,
				fill : new ol.style.Fill({
					color : lineColor
				})
			}),
			geometry : function(feature) {
				// return the coordinates of the first ring of the polygon
				var coordinates = feature.getGeometry().getCoordinates()[0];
				return new ol.geom.MultiPoint(coordinates);
			}
		})
	];
	return styles;
};

Collect.DataManager.MapPanelComposer.prototype.createBaseMapsLayer = function() {
	var worldTopoMapTileLayer = new ol.layer.Tile({
		// World Topographic Map
		title : 'Topographic map',
		type : 'base',
		visible : false,
		source : new ol.source.XYZ({
			attributions : [ new ol.Attribution({
				html : 'Tiles © <a href="https://services.arcgisonline.com/ArcGIS/' +
					'rest/services/World_Topo_Map/MapServer">ArcGIS</a>'
			}) ],
			url : 'https://server.arcgisonline.com/ArcGIS/rest/services/' +
				'World_Topo_Map/MapServer/tile/{z}/{y}/{x}'
		})
	});

	var satelliteMapTileLayer = new ol.layer.Tile({
		title : 'Satellite (ESRI)',
		type : 'base',
		visible : true,
		// Esri_WorldImagery
		source : new ol.source.XYZ({
			attributions : [ new ol.Attribution({
				html : 'Tiles &copy; Esri &mdash; Source: Esri, i-cubed, USDA, USGS, ' +
					'AEX, GeoEye, Getmapping, Aerogrid, IGN, IGP, UPR-EGP, and the GIS User Community'
			}) ],
			url : 'http://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}'
		})
	});

	var openStreetMapTileLayer = new ol.layer.Tile({
		title : 'Open Street Map',
		type : 'base',
		visible : false,
		source : new ol.source.OSM()
	});

	return new ol.layer.Group({
		title : 'Base maps',
		layers : [
			openStreetMapTileLayer,
			worldTopoMapTileLayer,
			satelliteMapTileLayer
		]
	});
}

Collect.DataManager.MapPanelComposer.prototype.onTileVisibleChange = function(event) {
	var $this = this;
	
	var tile = event.target;
	if (tile.getVisible()) {
		var survey = tile.get('survey');

		if (tile.getSource() == null) {
			switch (tile.get('type')) {
			case 'sampling_points':
				$this.createSamplingPointDataSource(survey, function(source) {
					tile.setSource(source);
				}, function(source) {
					$this.zoomToLayer(tile);
				});
				break;
			case 'coordinate_data':
				var coordinateAttributeDef = tile.get('coordinate_attribute_def');
				
				$this.createCoordinateDataSource(survey, coordinateAttributeDef, function(source) {
					tile.setSource(source);
				}, function(source) {
					$this.zoomToLayer(tile);
				});
				break;
			case 'geometry_data':
				var attributeDef = tile.get('attribute_def');
				
				$this.createGeometryDataSource(survey, attributeDef, function(source) {
					tile.setSource(source);
				}, function(source) {
					$this.zoomToLayer(tile);
				});
				break;
			}
		} else {
			$this.zoomToLayer(tile);
		}
	}

};

Collect.DataManager.MapPanelComposer.prototype.zoomToLayer = function(tile) {
	$this = this;
	if (tile.getSource() != null) {
		var extent = tile.getSource().getExtent();
		if (extent.length > 0 && isFinite(extent[0])) {
			$this.map.getView().fit(extent, {
				maxZoom: 7,
				duration: 2000
			});
		}
	}
}

Collect.DataManager.MapPanelComposer.prototype.createSamplingPointDataSource = function(survey, callback, readyCallback) {
	var url = OF.Strings.format("survey/{0}/sampling-point-data.kml", survey.id);
	var source = new ol.source.Vector({
		url : url,
		format : new ol.format.KML({
			extractStyles : false
		})
	});
	
	callback(source);
	
	//wait for load complete (change event)
	source.on('change', function(event) {
		if (source.getState() == 'ready') {
			onReady();
		}
	});
	
	function onReady() {
		source.forEachFeature(function(feature) {
			feature.setProperties({
				'type': 'sampling_point',
				'survey' : survey
			}, true);
		});
		readyCallback(source);
	}
	
	onReady();
};

Collect.DataManager.MapPanelComposer.prototype.createGeometryDataSource = function(survey, attributeDef, callback, readyCallback) {
	var rootEntityDefinitionId = survey.getMainRootEntity().id;
	
	var source = new ol.source.Vector();
	
	collect.dataService.countRecords(survey.id, rootEntityDefinitionId, function(recordCount) {
		if (recordCount == 0) {
			return;
		}
		var batchSize = 200;
		var maxProcessableItems = 1000 * 1000 * 1000;
		var totalItems = Math.min(recordCount, maxProcessableItems);

		var jobDialog = new OF.UI.JobDialog();

		var startTime = new Date().getTime();

		var extractVerticesFromKml = function(polygonKml) {
			var kmlDoc = $.parseXML(polygonKml);
			var $kml = $(kmlDoc)
			var $coordinatesStrEl = $kml.find("coordinates");
			var coordinatesStr = $coordinatesStrEl[0].textContent;
			var coordinates = coordinatesStr.split('\n');
			
			var vertices = new Array();
			coordinates.forEach(function(lonLatStr) {
				if (lonLatStr != null && lonLatStr != '') {
					var lonLatArr = lonLatStr.split(',');
					vertices.push(lonLatArr);
				}
			});
			return vertices;
		}
		
		var processGeometry = function(nodeInfo) {
			var vertices = extractVerticesFromKml(nodeInfo.geometry);
			
			var polygon = new ol.geom.Polygon([vertices]);
			
			var polygonFeature = new ol.Feature({
				survey : survey,
				geometry: polygon
			});
			source.addFeature(polygonFeature);
		};

		var processGeometries = function(geometries) {
			for (i = 0; i < geometries.length; i++) {
				processGeometry(geometries[i]);
			}
			
			callback(source);
			
			if (batchProcessor.progressPercent == 100) {
				jobDialog.close();
				readyCallback(source);
			} else {
				var fakeProgressJob = {
					status : "RUNNING",
					elapsedTime : new Date().getTime() - startTime,
					remainingMinutes : 0,
					progressPercent : batchProcessor.progressPercent
				};
				jobDialog.updateUI(fakeProgressJob);
				batchProcessor.processNext();
			}
		};

		var batchProcessor = new OF.Batch.BatchProcessor(totalItems, batchSize, function(blockOffset) {
			var srsId = 'EPSG:4326';
			collect.geoDataService.loadGeometryValues(survey.id, attributeDef.id, 
					srsId, blockOffset, batchSize, processGeometries);
		}, 500);

		jobDialog.cancelBtn.click(function() {
			batchProcessor.stop();
			jobDialog.close();
		});

		batchProcessor.start();
	});
};

Collect.DataManager.MapPanelComposer.prototype.createCoordinateDataSource = function(survey, coordinateAttributeDef, callback, readyCallback) {
	var rootEntityDefinitionId = survey.rootEntities[0].id;
	
	var source = new ol.source.Vector();
	
	collect.dataService.countRecords(survey.id, rootEntityDefinitionId, function(recordCount) {
		if (recordCount == 0) {
			return;
		}
		var batchSize = 200;
		var maxProcessableItems = 1000000000;
		var totalItems = Math.min(recordCount, maxProcessableItems);

		var jobDialog = new OF.UI.JobDialog();

		var startTime = new Date().getTime();

		var processCoordinateValue = function(coordinateAttributePoint) {
			var xyCoord = [ coordinateAttributePoint.x, coordinateAttributePoint.y ];
			
			var coordinateFeature = new ol.Feature({
				type : "coordinate_attribute_value",
				point : coordinateAttributePoint,
				survey : survey,
				geometry : new ol.geom.Point(xyCoord, 'XY')
			});
			source.addFeature(coordinateFeature);
		};

		var processCoordinateValues = function(coordinateAttributePoints) {
			for (i = 0; i < coordinateAttributePoints.length; i++) {
				processCoordinateValue(coordinateAttributePoints[i]);
			}
			
			callback(source);
			
			if (batchProcessor.progressPercent == 100) {
				jobDialog.close();
				readyCallback(source);
			} else {
				var fakeProgressJob = {
					status : "RUNNING",
					elapsedTime : new Date().getTime() - startTime,
					remainingMinutes : 0,
					progressPercent : batchProcessor.progressPercent
				};
				jobDialog.updateUI(fakeProgressJob);
				batchProcessor.processNext();
			}
		};

		var batchProcessor = new OF.Batch.BatchProcessor(totalItems, batchSize, function(blockOffset) {
			var srsId = 'EPSG:4326';
			collect.geoDataService.loadCoordinateValues(survey.id, coordinateAttributeDef.id, 
					srsId, blockOffset, batchSize, processCoordinateValues);
		}, 500);

		jobDialog.cancelBtn.click(function() {
			batchProcessor.stop();
			jobDialog.close();
		});

		batchProcessor.start();
	});
};

function intToRGB(i) {
	var c = (i & 0x00FFFFFF)
		.toString(16)
		.toLowerCase();

	return "00000".substring(0, 6 - c.length) + c;
}

function stringToColor(s) {
	return "#" + this.intToRGB(OF.Strings.hashCode(s));
}

function getRandomColor(minimum, maximum) {
	if (! min) {
		min = '#000000';
	}
	if (! max) {
		max = '#FFFFFF';
	}
	var result;
	do {
		result = '#'+('00000'+(Math.random()*(1<<24)|0).toString(16)).slice(-6);
	} while (result < min || result > max);
	return result;
}

function defaultIfNull(value, defaultValue) {
	return value ? value : defaultValue;
}

function getRandomRGBColor(rMin, rMax, gMin, gMax, bMin, bMax) {
	rMin = defaultIfNull(rMin, 0);
	rMax = defaultIfNull(rMax, 255);
	gMin = defaultIfNull(gMin, 0);
	gMax = defaultIfNull(gMax, 255);
	bMin = defaultIfNull(bMin, 0);
	bMax = defaultIfNull(bMax, 255);
	var result = [getRandomValue(rMin, rMax), getRandomValue(gMin, gMax), getRandomValue(bMin, bMax)];
	return result;
	
	function getRandomValue(min, max) {
		var result = min + Math.random() * (max - min);
		return result;
	}
}

Collect.DataManager.MapPanelComposer.prototype.reset = function() {
	//this.map.remove();
	this.init();
}

Collect.DataManager.MapPanelComposer.prototype.onPanelShow = function() {
	var $this = this;
	if ($this.map == null) {
		$this.resizeMapContainer();
		$this.init();
	}
}

Collect.DataManager.MapPanelComposer.prototype.onSurveyChanged = function() {
}

Collect.DataManager.MapPanelComposer.prototype.resizeMapContainer = function() {
	$("#map").height($(window).height() - this.verticalPadding);
	$("#map").width($(window).width() - this.horizontalPadding);
}

Collect.DataManager.MapPanelComposer.openRecordEditPopUp = function(surveyId, recordId, recordKeyString) {
	var modalContainer = $("#record-edit-modal");
	var modalContent = modalContainer.find(".modal-content");
	var recordKeyLabel = modalContainer.find(".record-key-label");
	var iframe = modalContainer.find("iframe");

	recordKeyLabel.text(recordKeyString);

	modalContent.resizable({
		alsoRezize : ".modal-body"
	});
	modalContent.draggable();

	function setInitialRecordEditPopUpSize() {
		iframe.prop("height", $(window).height() - 180);
	}

	function resizeIFrame() {
		iframe.prop("height", modalContent.height() - 100);
	}

	$(modalContainer).on("resize", resizeIFrame);

	modalContainer.on('show.bs.modal', function() {
		iframe.attr("src", "index.htm?edit=true"
			+ "&surveyId=" + surveyId
			+ "&recordId=" + recordId
			+ "&locale=" + OF.i18n.currentLocale());
		$(this).find('.modal-body').css({
			'max-height' : '100%'
		});
	});
	var options = {
		backdrop : "static",
		keyboard : false
	};
	modalContainer.modal(options);

	setInitialRecordEditPopUpSize();
};