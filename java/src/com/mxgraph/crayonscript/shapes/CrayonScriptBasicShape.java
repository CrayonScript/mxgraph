package com.mxgraph.crayonscript.shapes;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.model.CellFrameEnum;
import com.mxgraph.model.CellGapEnum;
import com.mxgraph.model.CellPaintMode;
import com.mxgraph.model.mxCell;
import com.mxgraph.shape.mxDefaultTextShape;
import com.mxgraph.shape.mxStencilShape;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Logger;

public abstract class CrayonScriptBasicShape implements CrayonScriptIShape
{
	public CrayonScriptBasicShape(ShapeStructureType shapeStructureType, String shapeName)
	{
		this.shapeStructureType = shapeStructureType;
		this.shapeName = shapeName;
	}

	protected String shapeName;

	protected ShapeStructureType shapeStructureType;

	protected mxDefaultTextShape textShape = new mxDefaultTextShape();

	protected boolean isTemplate = false;

	protected transient ArrayList<Color> currentColors;

	protected static Map<ShapeStructureType, ArrayList<SvgElement>> svgElementsMap;

	protected static Map<ShapeStructureType, HashMap<CellGapEnum, Point2D>> svgElementsGapsMap;

	private static final Logger log = Logger.getLogger(mxStencilShape.class.getName());

	protected static boolean initialized;

	public static HashMap<String, Object> TEXT_STYLE = new HashMap<>();

	static {
		TEXT_STYLE.put(mxConstants.STYLE_FONTFAMILY, mxConstants.DEFAULT_FONTFAMILY);
		TEXT_STYLE.put(mxConstants.STYLE_FONTSIZE, 24);
		TEXT_STYLE.put(mxConstants.STYLE_FONTSTYLE, 0);
		TEXT_STYLE.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
		TEXT_STYLE.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);

		initialize();
	}

	public ArrayList<SvgElement> getSvgElements()
	{
		return svgElementsMap.get(shapeStructureType);
	}

	public HashMap<CellGapEnum, Point2D> getSvgElementGaps()
	{
		return svgElementsGapsMap.get(shapeStructureType);
	}

	public double getOriginalGap(CellGapEnum gapEnum)
	{
		HashMap<CellGapEnum, Point2D> gaps = getSvgElementGaps();
		Point2D gap = gaps.get(gapEnum);
		return gap.getY();
	}

	public int getSubElements()
	{
		ArrayList<SvgElement> svgElements = getSvgElements();
		return (svgElements == null) ? 0 : (svgElements.size() - 1);
	}

	public Color getParentFrameColor(mxCellState state)
	{
		Color frameColor = getParentFrameColor((mxCell) state.getCell());
        if (frameColor == null)
        {
            frameColor = getFrameColor();
        }
        return frameColor;
	}

	public Color getFrameColor()
	{
		ArrayList<SvgElement> svgElements = getSvgElements();
		SvgElement svgElement = svgElements.get(0);
		return svgElement.fillColor;
	}

	public Color getFrameColor(CellFrameEnum frameEnum)
	{
		ArrayList<SvgElement> svgElements = getSvgElements();
		SvgElement svgElement = svgElements.get(frameEnum.bitIndex);
		return svgElement.fillColor;
	}

	public Color getParentFrameColor(mxCell cell)
	{
		mxCell parentCell = (mxCell) cell.getParent();
		if (parentCell == null) return null;
		CrayonScriptIShape referenceShape = parentCell.referenceShape;
		if (parentCell.referenceShape == null) return null;
		return referenceShape.getFrameColor();
	}

	public RoundRectangle2D getFrame(CellFrameEnum frameEnum)
	{
		SvgElement svgElement = getSvgElements().get(frameEnum.bitIndex);
		return svgElement.rect;
	}

	public boolean isExtender() { return false; }

	protected void initialize(mxCellState state)
	{
		checkTemplate(state);
		initializeRectangles(state);
	}

	protected void initializeRectangles(mxCellState state)
	{
		mxCell cell = (mxCell) state.getCell();

		if (currentColors == null)
		{
			currentColors = new ArrayList<>();
			ArrayList<SvgElement> svgElements = getSvgElements();
			for (int i = 0; i < svgElements.size(); i++) {
				currentColors.add(svgElements.get(i).fillColor);
			}
		}
	}

	protected static ArrayList<SvgElement> buildSvgElementsFor(String typeStr)
	{
		String resourceStr = String.format("/com/mxgraph/crayonscript/images/%s.svg", typeStr);
		URL resourceUrl = CrayonScriptBasicShape.class.getResource(resourceStr);
		assert resourceUrl != null;
		return readSvgElements(resourceUrl);
	}

	protected static void initialize() {
		if (initialized) return;
		ArrayList<SvgElement> svgElements;
		svgElementsMap = new HashMap<>();

		svgElements = buildSvgElementsFor("Event");
		svgElementsMap.put(ShapeStructureType.EVENT, svgElements);

		svgElements = buildSvgElementsFor("Assign");
		svgElementsMap.put(ShapeStructureType.ASSIGN, svgElements);

		svgElements = buildSvgElementsFor("Property");
		svgElementsMap.put(ShapeStructureType.PROPERTY, svgElements);

		svgElements = buildSvgElementsFor("Function");
		svgElementsMap.put(ShapeStructureType.FUNCTION, svgElements);

		svgElements = buildSvgElementsFor("WaitFor");
		svgElementsMap.put(ShapeStructureType.WAIT_FOR, svgElements);

		svgElements = buildSvgElementsFor("If");
		svgElementsMap.put(ShapeStructureType.IF, svgElements);

		svgElements = buildSvgElementsFor("ElseIf");
		svgElementsMap.put(ShapeStructureType.ELSE_IF, svgElements);

		svgElements = buildSvgElementsFor("While");
		svgElementsMap.put(ShapeStructureType.WHILE, svgElements);

		svgElements = buildSvgElementsFor("For");
		svgElementsMap.put(ShapeStructureType.FOR, svgElements);

		svgElements = buildSvgElementsFor("Parallel");
		svgElementsMap.put(ShapeStructureType.PARALLEL, svgElements);

		svgElements = buildSvgElementsFor("Sequential");
		svgElementsMap.put(ShapeStructureType.SEQUENTIAL, svgElements);

		svgElements = buildSvgElementsFor("Run");
		svgElementsMap.put(ShapeStructureType.RUN, svgElements);

		svgElements = buildSvgElementsFor("ParallelVExtender");
		svgElementsMap.put(ShapeStructureType.PARALLEL_VEXTENDER, svgElements);

		svgElements = buildSvgElementsFor("SequentialVExtender");
		svgElementsMap.put(ShapeStructureType.SEQUENTIAL_VEXTENDER, svgElements);

		svgElements = buildSvgElementsFor("Expression");
		svgElementsMap.put(ShapeStructureType.EXPRESSION, svgElements);

		svgElements = buildSvgElementsFor("Marker");
		svgElementsMap.put(ShapeStructureType.MARKER, svgElements);

		svgElements = buildSvgElementsFor("Template");
		svgElementsMap.put(ShapeStructureType.TEMPLATE, svgElements);

		svgElementsGapsMap = new HashMap<>();
		for (Map.Entry<ShapeStructureType, ArrayList<SvgElement>> entry: svgElementsMap.entrySet()) {
			ArrayList<SvgElement> gapElements = entry.getValue();
			HashMap<CellGapEnum, Point2D> gapMap = new HashMap<>();
			svgElementsGapsMap.put(entry.getKey(), gapMap);

			{
				double gap = gapElements.get(0).rect.getHeight();
				gapMap.put(CellGapEnum.OUTER_TOP_TO_OUTER_BOTTOM, new Point2D.Double(0, gap));
			}

			{
				double gap = gapElements.size() > 1 ?
						(
								getInner1Top(gapElements) - getOuterTop(gapElements)
						)
						: 0;
				gapMap.put(CellGapEnum.OUTER_TOP_TO_INNER_1_TOP, new Point2D.Double(0, gap));
			}

			{
				double gap = gapElements.size() > 1 ?
						(
								getInner1Bottom(gapElements) - getOuterTop(gapElements)
						)
						: 0;
				gapMap.put(CellGapEnum.OUTER_TOP_TO_INNER_1_BOTTOM, new Point2D.Double(0, gap));
			}

			{
				double gap = gapElements.size() > 2 ?
						(
								getInner2Top(gapElements) - getOuterTop(gapElements)
						)
						: 0;
				gapMap.put(CellGapEnum.OUTER_TOP_TO_INNER_2_TOP, new Point2D.Double(0, gap));
			}

			{
				double gap = gapElements.size() > 2 ?
						(
								getInner2Bottom(gapElements) - getOuterTop(gapElements)
						)
						: 0;
				gapMap.put(CellGapEnum.OUTER_TOP_TO_INNER_2_BOTTOM, new Point2D.Double(0, gap));
			}

			{
				double gap = gapElements.size() > 2 ?
						(
								getInner2Top(gapElements) - getInner1Top(gapElements)
						)
						: 0;
				gapMap.put(CellGapEnum.INNER_1_TOP_TO_INNER_2_TOP, new Point2D.Double(0, gap));
			}

			{
				double gap = gapElements.size() > 2 ?
						(
								getInner2Bottom(gapElements) - getInner1Top(gapElements)
						)
						: 0;
				gapMap.put(CellGapEnum.INNER_1_TOP_TO_INNER_2_BOTTOM, new Point2D.Double(0, gap));
			}

			{
				double gap = gapElements.size() > 2 ?
						(
								getOuterBottom(gapElements) - getInner1Top(gapElements)
						)
						: 0;
				gapMap.put(CellGapEnum.INNER_1_TOP_TO_OUTER_BOTTOM, new Point2D.Double(0, gap));
			}

			{
				double gap = gapElements.size() > 2 ?
						(
								getInner2Top(gapElements) - getInner1Bottom(gapElements)
						)
						: 0;
				gapMap.put(CellGapEnum.INNER_1_BOTTOM_TO_INNER_2_TOP, new Point2D.Double(0, gap));
			}

			{
				double gap = gapElements.size() > 2 ?
						(
								getInner2Bottom(gapElements) - getInner1Bottom(gapElements)
						)
						: 0;
				gapMap.put(CellGapEnum.INNER_1_BOTTOM_TO_INNER_2_BOTTOM, new Point2D.Double(0, gap));
			}

			{
				double gap = gapElements.size() > 2 ?
						(
								getOuterBottom(gapElements) - getInner1Bottom(gapElements)
						)
						: 0;
				gapMap.put(CellGapEnum.INNER_1_BOTTOM_TO_OUTER_BOTTOM, new Point2D.Double(0, gap));
			}

			{
				double gap = gapElements.size() > 2 ?
						(
								getOuterBottom(gapElements) - getInner2Top(gapElements)
						)
						: 0;
				gapMap.put(CellGapEnum.INNER_2_TOP_TO_OUTER_BOTTOM, new Point2D.Double(0, gap));
			}

			{
				double gap = gapElements.size() > 2 ?
						(
								getOuterBottom(gapElements) - getInner2Bottom(gapElements)
						)
						: 0;
				gapMap.put(CellGapEnum.INNER_2_BOTTOM_TO_OUTER_BOTTOM, new Point2D.Double(0, gap));
			}
		}

		initialized = true;
	}

	protected static double getSvgElementTop(ArrayList<SvgElement> svgElements, int elementIndex)
	{
		if (svgElements.size() <= elementIndex) return 0;
		return (svgElements.get(elementIndex).rect.getY());
	}

	protected static double getSvgElementBottom(ArrayList<SvgElement> svgElements, int elementIndex)
	{
		if (svgElements.size() <= elementIndex) return 0;
		return (svgElements.get(elementIndex).rect.getY() + svgElements.get(elementIndex).rect.getHeight());
	}

	protected static double getOuterTop(ArrayList<SvgElement> svgElements)
	{
		return getSvgElementTop(svgElements, 0);
	}

	protected static double getOuterBottom(ArrayList<SvgElement> svgElements)
	{
		return getSvgElementBottom(svgElements, 0);
	}

	protected static double getInner1Top(ArrayList<SvgElement> svgElements)
	{
		return getSvgElementTop(svgElements, 1);
	}

	protected static double getInner1Bottom(ArrayList<SvgElement> svgElements)
	{
		return getSvgElementBottom(svgElements, 1);
	}

	protected static double getInner2Top(ArrayList<SvgElement> svgElements)
	{
		return getSvgElementTop(svgElements, 2);
	}

	protected static double getInner2Bottom(ArrayList<SvgElement> svgElements)
	{
		return getSvgElementBottom(svgElements, 2);
	}

	public static SvgElement getTemplate()
	{
		ArrayList<SvgElement> svgElements = svgElementsMap.get(ShapeStructureType.TEMPLATE);
		return svgElements.get(0);
	}

	protected void checkTemplate(mxCellState state)
	{
		isTemplate = ((mxCell) state.getCell()).isAncestorTemplate();
	}

	protected static ArrayList<SvgElement> readSvgElements(URL url, CellPaintMode paintMode) {
		ArrayList<SvgElement> svgElements = readSvgElements(url);
		switch (paintMode)
		{
			case FRAME_IN_FRAME:
				// adjust the svg elements such that the top of the outer frame is aligned with the top of the first child
				// and bottom of the outer frame is aligned with the bottom of the last child
				if (svgElements.size() > 1)
				{
					ArrayList<SvgElement> modifiedSvgElements = new ArrayList<>();
					SvgElement outer = svgElements.get(0);
					SvgElement first = svgElements.get(1);
					SvgElement last = svgElements.get(svgElements.size()-1);
					outer = outer.copy();
					outer.rect.setFrame(
							outer.rect.getX(),
							first.rect.getY(),
							outer.rect.getWidth(),
							last.rect.getY() + last.rect.getHeight() - first.rect.getY()
					);
					modifiedSvgElements.add(outer);
					for (int i = 1; i < svgElements.size(); i++) {
						modifiedSvgElements.add(svgElements.get(i));
					}
					svgElements = modifiedSvgElements;
				}
			case DEFAULT:
			default:
				break;
		}
		return svgElements;
	}

	protected static ArrayList<SvgElement> readSvgElements(URL url) {
		ArrayList<SvgElement> svgElements = new ArrayList<>();
		Document doc = mxUtils.loadDocument(url.toString());
		assert doc != null;
		Node rootElement = doc.getDocumentElement();
		Stack<Node> childNodes = new Stack<>();
		childNodes.push(rootElement);
		while (!childNodes.empty())
		{
			Node childNode = childNodes.pop();
			SvgElement svgElement = new SvgElement(childNode);
			if (svgElement.svgElementType == SvgElementType.RECTANGLE)
			{
				svgElement.parseRectangle(childNode);
				svgElements.add(svgElement);
			}
			NodeList subChildNodeList = childNode.getChildNodes();
			if (subChildNodeList.getLength() > 0)
			{
				childNodes.push(subChildNodeList.item(0));
			}
			Node nextSiblingNode = childNode.getNextSibling();
			if (nextSiblingNode != null)
			{
				childNodes.push(nextSiblingNode);
			}
		}
		return svgElements;
	}

	protected void paintRectangle(mxGraphics2DCanvas canvas,
								  mxCellState cellState,
								  int index,
								  Color fillColor)
	{
		paintRectangle(canvas, cellState, index, fillColor, false);
	}

	protected void paintRectangle(mxGraphics2DCanvas canvas,
								  mxCellState cellState,
								  int index,
								  Color fillColor,
								  boolean isFrame)
	{
		paintRectangle(canvas, cellState, index, fillColor, isFrame, false);
	}

	protected void paintRectangle(mxGraphics2DCanvas canvas,
								  mxCellState cellState,
								  int index,
								  Color fillColor,
								  boolean isFrame,
								  boolean isExtender)
	{
		if (fillColor != null)
		{
			canvas.getGraphics().setColor(fillColor);
		}

		ArrayList<RoundRectangle2D> currentRoundRectangles = cellState.getCurrentRoundRectangles();
		RoundRectangle2D roundedRect = currentRoundRectangles.get(index);

		if (isFrame)
		{
			CellPaintMode paintMode = ((mxCell) cellState.getCell()).calcPaintMode();

			if (paintMode == CellPaintMode.DEFAULT)
			{
				Path2D path = isExtender ? getFrameExtenderPath(roundedRect) : getFramePath(roundedRect);
				canvas.getGraphics().fill(path);
				canvas.getGraphics().draw(path);
			}
			else
			{
				RoundRectangle2D first = currentRoundRectangles.get(1);
				RoundRectangle2D last = currentRoundRectangles.get(currentRoundRectangles.size()-1);
				double height = last.getY() + last.getHeight() - first.getY();
				canvas.getGraphics().fillRect(
						(int) roundedRect.getX(),
						(int) first.getY(),
						(int) roundedRect.getWidth(),
						(int) (height));
			}
		}
		else
		{
			canvas.getGraphics().fillRect(
					(int) roundedRect.getX(),
					(int) roundedRect.getY(),
					(int) roundedRect.getWidth(),
					(int) roundedRect.getHeight());
		}
	}

	protected void drawText(mxGraphics2DCanvas canvas, String text, mxCellState state)
	{
		Rectangle stateRect = state.getRectangle();
		mxRectangle rect = new mxRectangle(
				stateRect.getX(),
				stateRect.getY(),
				stateRect.getWidth(),
				stateRect.getHeight());
		state.setLabelBounds(rect);
		textShape.paintTextShape(canvas, text, state, TEXT_STYLE);
	}

	protected Color getColor(Color color)
	{
		if (color == null) return null;
		int opacity = getOpacity();
		return getColor(color, opacity);
	}

	protected Color getColor(Color color, int opacity)
	{
		if (color == null) return null;
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
	}

	public int getOpacity()
	{
		return isTemplate ? 255 : 85;
	}

	public static RoundRectangle2D scaleRectangle(
			mxCellState state,
			SvgElement root,
			SvgElement target,
			CellPaintMode paintMode)
	{
		return scaleRectangle(state, root, target);
	}

	public static RoundRectangle2D scaleRectangle(
			mxCellState state,
			SvgElement root,
			SvgElement target)
	{
		Rectangle stateRect = state.getRectangle();
		return scaleRectangle(stateRect, root, target);
	}

	public static RoundRectangle2D scaleRectangle(
			Rectangle stateRect,
			SvgElement root,
			SvgElement target)
	{
		return scaleRectangle(stateRect, root.getRect(), target.getRect());
	}

	public static RoundRectangle2D scaleRectangle(
			Rectangle stateRect,
			RoundRectangle2D root,
			RoundRectangle2D target)
	{
		double widthRatio = stateRect.getWidth() / root.getWidth();
		double heightRatio = stateRect.getHeight() / root.getHeight();

		int x = (int) (stateRect.x + (target.getX() - root.getX()) * widthRatio);
		int y = (int) (stateRect.y + (target.getY() - root.getY()) * heightRatio);
		int w = (int) (target.getWidth() * widthRatio);
		int h = (int) (target.getHeight() * heightRatio);
		int rw = (int) (target.getArcWidth() * widthRatio);
		int rh = (int) (target.getArcHeight() * heightRatio);

		return new RoundRectangle2D.Double(x, y, w, h, rw, rh);
	}

	public static Path2D getFramePath(RoundRectangle2D roundedRect)
	{
		double x = roundedRect.getX();
		double y = roundedRect.getY();
		double w = roundedRect.getWidth();
		double h = roundedRect.getHeight();
		double rx = roundedRect.getArcWidth();
		double ry = roundedRect.getArcHeight();

		double normalizedRxSize = 36;
		double normalizedArcSize = 36;
		double arcSize = normalizedArcSize * rx / normalizedRxSize;

		Path2D path = new Path2D.Double();
		path.moveTo(x, y + arcSize);
		path.lineTo(x, y + h);
		path.lineTo(x + w / 2 - arcSize / 2 - 3, y + h);
		path.curveTo(x + w / 2 - arcSize / 2, y + h - arcSize, x + w / 2 + arcSize / 2, y + h - arcSize, x + w / 2 + arcSize / 2 + 3, y + h);
		path.lineTo(x + w, y + h);
		path.lineTo(x + w, y + arcSize);
		path.lineTo(x + w / 2 + arcSize / 2, y + arcSize);
		path.curveTo(x + w / 2 + arcSize / 2, y, x + w / 2 - arcSize / 2, y, x + w / 2 - arcSize / 2, y + arcSize);
		path.lineTo(x, y + arcSize);
		path.closePath();

		return path;
	}

	public static Path2D getFrameExtenderPath(RoundRectangle2D roundedRect)
	{
		double x = roundedRect.getX();
		double y = roundedRect.getY();
		double w = roundedRect.getWidth();
		double h = roundedRect.getHeight();
		double rx = roundedRect.getArcWidth();
		double ry = roundedRect.getArcHeight();

		double normalizedRxSize = 36;
		double normalizedArcSize = 36;
		double arcSize = normalizedArcSize * rx / normalizedRxSize;

		Path2D path = new Path2D.Double();
		path.moveTo(x, y + arcSize);
		path.lineTo(x, y + h);
		path.lineTo(x + w / 2 - arcSize / 2 - 3, y + h);
		path.curveTo(x + w / 2 - arcSize / 2, y + h - arcSize, x + w / 2 + arcSize / 2, y + h - arcSize, x + w / 2 + arcSize / 2 + 3, y + h);
		path.lineTo(x + w, y + h);
		path.lineTo(x + w, y + arcSize);
		path.lineTo(x, y + arcSize);
		path.closePath();

		return path;
	}

	protected enum SvgElementType {
		OTHER,
		RECTANGLE,
		GROUP
	}

	protected enum ShapeStructureType {
		WAIT_FOR,
		EVENT,
		ASSIGN,
		FUNCTION,
		PARALLEL,
		PROPERTY,
		RUN,
		SEQUENTIAL,
		EXPRESSION,
		IF,
		ELSE_IF,
		FOR,
		WHILE,
		PARALLEL_VEXTENDER,
		SEQUENTIAL_VEXTENDER,
		MARKER,
		TEMPLATE,
	}

	public static class SvgElement implements Cloneable
	{
		Color fillColor = null;
		Color strokeColor = null;
		RoundRectangle2D rect = null;
		private SvgElementType svgElementType = SvgElementType.OTHER;

		public SvgElement(Node node)
		{
			String tag = node.getNodeName();
			if (isGroup(tag))
			{
				svgElementType = SvgElementType.GROUP;
			}
			else if (isRectangle(tag))
			{
				svgElementType = SvgElementType.RECTANGLE;
			}
		}

		public RoundRectangle2D getRect()
		{
			return rect;
		}

		public SvgElement copy()
		{
			try
			{
				return (SvgElement) clone();
			}
			catch(CloneNotSupportedException e)
			{
				throw new IllegalStateException(e);
			}
		}

		@Override
		protected Object clone() throws CloneNotSupportedException {

			SvgElement clone = (SvgElement) super.clone();
			clone.fillColor = fillColor == null ? null
					: new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), fillColor.getAlpha());
			clone.strokeColor = strokeColor == null ? null
					: new Color(strokeColor.getRed(), strokeColor.getGreen(), strokeColor.getBlue(), strokeColor.getAlpha());
			clone.rect = rect == null ? null
					: new RoundRectangle2D.Double(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), rect.getArcWidth(), rect.getArcHeight());
			clone.svgElementType = svgElementType;

			return clone;
		}

		private boolean isRectangle(String tag)
		{
			return tag != null && (tag.equals("svg:rect") || tag.equals("rect"));
		}

		private boolean isGroup(String tag)
		{
			return tag != null && (tag.equals("svg:g") || tag.equals("g"));
		}

		protected void parseRectangle(Node node)
		{
			NamedNodeMap attributes = node.getAttributes();
			if (attributes != null)
			{
				double x = 0.0, y = 0.0, width = 0.0, height = 0.0, rx = 0.0, ry = 0.0;
				Node xNode = attributes.getNamedItem("x");
				if (xNode != null) x = Double.parseDouble(xNode.getNodeValue());
				Node yNode = attributes.getNamedItem("y");
				if (yNode != null) y = Double.parseDouble(yNode.getNodeValue());
				Node widthNode = attributes.getNamedItem("width");
				if (widthNode != null) width = Double.parseDouble((widthNode.getNodeValue()));
				Node heightNode = attributes.getNamedItem("height");
				if (heightNode != null) height = Double.parseDouble(heightNode.getNodeValue());
				Node rxNode = attributes.getNamedItem("rx");
				if (rxNode != null) rx = Double.parseDouble(rxNode.getNodeValue());
				Node ryNode = attributes.getNamedItem("ry");
				if (ryNode != null) ry = Double.parseDouble(ryNode.getNodeValue());
				rect = new RoundRectangle2D.Double(x, y, width, height, rx, ry);

				String fill = "none", stroke = "none";
				Node fillNode = attributes.getNamedItem("fill");
				if (fillNode != null) fill = fillNode.getNodeValue();
				if (!"none".equals(fill))  fillColor = Color.decode("0x" + fill.replace("#", ""));
				Node strokeNode = attributes.getNamedItem("stroke");
				if (strokeNode != null) stroke = strokeNode.getNodeValue();
				if (!"none".equals(stroke))  strokeColor = Color.decode("0x" + stroke.replace("#", ""));
			}
		}
	}

}
