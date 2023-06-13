/**
 * Returns the current scroll position of the page
 * @return {number} Scroll position of the page
 */
function getCurrentScrollPosition() {
	return window.pageYOffset || document.documentElement.scrollTop;
}

/**
 * Returns the entire height of the document
 * @return {number} The normalized document height of the document
 */
function getDocumentHeight() {
	const heights = [
		document.body.clientHeight,
		document.documentElement.clientHeight,
		document.documentElement.scrollHeight,
	];

	return Math.max(...heights);
}

function getDimensions(element) {
	if (element) {
		const boundingClientRect = element.getBoundingClientRect();
		const {bottom, height, top} = boundingClientRect;
		return {bottom, height, top};
	}
	const height = getDocumentHeight();
	const top = getCurrentScrollPosition();
	return {height, top};
}

class ScrollTracker {
	constructor(steps = 4) {
		this.steps = steps;
		this.stepsReached = [];
	}

	dispose() {
		this.steps = null;
		this.stepsReached = null;
	}

	getDepthValue(element) {
		const {bottom, height, top} = getDimensions(element);
		const visibleArea = window.innerHeight;
		let depthValue = (visibleArea - top) / height;

		if (top <= 0 && bottom >= 0) {
			depthValue = visibleArea / (height + top);
		} else if (!element) {
			depthValue = (top + visibleArea) / height;
		}

		return depthValue;
	}

	/**
	 * Calculates the depth of the element on the page. If the
	 * element is not passed as a parameter the calculation must be
	 * performed to get the page depth
	 * @param {object} element The Blog DOM element
	 * @return {number} depth percentage from 0 to 100
	 */
	getDepth(element) {
		const value = this.getDepthValue(element);
		const depth = Math.round(value * 100);

		return Math.min(Math.max(depth, 0), 100);
	}

	/**
	 * Processes the current scroll location and calculates the scroll depth level
	 * If the client reaches one of the pre-defined levels that is yet unreached
	 * it sends an analytics event
	 * @param {Function} The callback function that will process the depth reached.
	 */
	onDepthReached(fn, element) {
		const depth = this.getDepth(element);
		const step = Math.floor(100 / this.steps);

		const depthLevel = Math.floor(depth / step);

		if (!this.stepsReached.some(val => val === depthLevel)) {
			this.stepsReached.push(depthLevel);

			if (depthLevel > 0) {
				fn(depthLevel * step);
			}
		}
	}
}

export {ScrollTracker};