/**
 * Class: mxCrayonScriptTemplateShape
 *
 * Extends <mxShape> to implement a rectangle shape.
 * This shape is registered under <mxConstants.SHAPE_CRAYONSCRIPT>
 * in <mxCellRenderer>.
 * 
 * Constructor: mxCrayonScriptTemplateShape
 *
 * Constructs a new crayonscript template shape.
 * 
 * Parameters:
 * 
 * bounds - <mxCrayonTemplateScript> that defines the bounds. This is stored in
 * <mxShape.bounds>.
 * fill - String that defines the fill color. This is stored in <fill>.
 * stroke - String that defines the stroke color. This is stored in <stroke>.
 * strokewidth - Optional integer that defines the stroke width. Default is
 * 1. This is stored in <strokewidth>.
 */
function mxCrayonScriptTemplateShape(bounds, fill, stroke, strokewidth)
{
	mxShape.call(this);
	this.bounds = bounds;
	this.fill = fill;
	this.stroke = stroke;
	this.strokewidth = (strokewidth != null) ? strokewidth : 1;
};

/**
 * Extends mxShape.
 */
mxUtils.extend(mxCrayonScriptTemplateShape, mxShape);

/**
 * Function: isHtmlAllowed
 *
 * Returns true for non-rounded, non-rotated shapes with no glass gradient.
 */
mxCrayonScriptTemplateShape.prototype.isHtmlAllowed = function()
{
	var events = true;
	
	if (this.style != null)
	{
		events = mxUtils.getValue(this.style, mxConstants.STYLE_POINTER_EVENTS, '1') == '1';		
	}
	
	return !this.isRounded && !this.glass && this.rotation == 0 && (events ||
		(this.fill != null && this.fill != mxConstants.NONE));
};

/**
 * Function: paintBackground
 * 
 * Generic background painting implementation.
 */
mxCrayonScriptTemplateShape.prototype.paintBackground = function(c, x, y, w, h)
{
	var events = true;
	
	if (this.style != null)
	{
		events = mxUtils.getValue(this.style, mxConstants.STYLE_POINTER_EVENTS, '1') == '1';
	}
	
	if (events || (this.fill != null && this.fill != mxConstants.NONE) ||
		(this.stroke != null && this.stroke != mxConstants.NONE))
	{
		if (!events && (this.fill == null || this.fill == mxConstants.NONE))
		{
			c.pointerEvents = false;
		}

		if (this.isCrayonScriptBlock)
		{
		    var r = 0;

            if (mxUtils.getValue(this.style, mxConstants.STYLE_ABSOLUTE_ARCSIZE, 0) == '1')
            {
                r = Math.min(w / 2, Math.min(h / 2, mxUtils.getValue(this.style,
                    mxConstants.STYLE_ARCSIZE, mxConstants.LINE_ARCSIZE) / 2));
            }
            else
            {
                var f = mxUtils.getValue(this.style, mxConstants.STYLE_ARCSIZE,
                    mxConstants.RECTANGLE_ROUNDING_FACTOR * 100) / 100;
                r = Math.min(w * f, h * f);
            }

            var arc = r;

            c.begin();

		    c.setLineJoin("bevel");

            c.moveTo(x + arc, y);
            c.quadTo(x, y, x, y + arc);
            c.lineTo(x, y + h - arc);
            c.quadTo(x, y + h, x + arc, y + h);
            c.lineTo(x + w * 0.4, y + h);
            c.quadTo(x + w * 0.5, y + h * 0.8, x + w * 0.6, y + h);
            c.lineTo(x + w - arc, y + h);
            c.quadTo(x + w, y + h, x + w, y + h - arc);
            c.lineTo(x + w, y + arc);
            c.quadTo(x + w, y, x + w - arc, y);
            c.lineTo(x + w * 0.6, y);
            c.quadTo(x + w * 0.5, y - h * 0.2, x + w * 0.4, y);


		    c.close();
		}
		else if (this.isRounded)
		{
			var r = 0;
			
			if (mxUtils.getValue(this.style, mxConstants.STYLE_ABSOLUTE_ARCSIZE, 0) == '1')
			{
				r = Math.min(w / 2, Math.min(h / 2, mxUtils.getValue(this.style,
					mxConstants.STYLE_ARCSIZE, mxConstants.LINE_ARCSIZE) / 2));
			}
			else
			{
				var f = mxUtils.getValue(this.style, mxConstants.STYLE_ARCSIZE,
					mxConstants.RECTANGLE_ROUNDING_FACTOR * 100) / 100;
				r = Math.min(w * f, h * f);
			}
			
			c.roundrect(x, y, w, h, r, r);
		}
		else
		{
			c.rect(x, y, w, h);
		}
			
		c.fillAndStroke();
	}
};

/**
 * Function: isRoundable
 * 
 * Adds roundable support.
 */
mxCrayonScriptTemplateShape.prototype.isRoundable = function(c, x, y, w, h)
{
	return true;
};

/**
 * Function: paintForeground
 * 
 * Generic background painting implementation.
 */
mxCrayonScriptTemplateShape.prototype.paintForeground = function(c, x, y, w, h)
{
	if (this.glass && !this.outline && this.fill != null && this.fill != mxConstants.NONE)
	{
		this.paintGlassEffect(c, x, y, w, h, this.getArcSize(w + this.strokewidth, h + this.strokewidth));
	}
};
