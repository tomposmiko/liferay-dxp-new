import MarkerBase from '../../src/main/resources/META-INF/resources/js/MarkerBase.es';

describe('MarkerBase', () => {
	let markerChild;

	class MarkerChild extends MarkerBase {
		_getNativeMarker(location, map) {
			return {name: 'NativeMarker', location, map};
		}

		_getNormalizedEventData(nativeEvent) {
			return {name: 'NormalizedEventData', nativeEvent};
		}
	}

	beforeEach(() => {
		markerChild = new MarkerChild();

		jest.spyOn(markerChild, '_getNativeMarker');
		jest.spyOn(markerChild, '_getNormalizedEventData');
		jest.spyOn(markerChild, '_handleNativeEvent');
		jest.spyOn(markerChild, 'emit');
	});

	describe('_handleNativeEvent', () => {
		it('should call _getNormalizedEventData with the given event', () => {
			markerChild._handleNativeEvent({name: 'NativeEvent'}, 'NonNativeEvent');

			expect(markerChild._getNormalizedEventData).toHaveBeenCalledTimes(1);
			expect(markerChild._getNormalizedEventData.mock.calls[0]).toEqual([
				{name: 'NativeEvent'},
			]);
		});

		it('should emit an event with the given event type', () => {
			markerChild._handleNativeEvent({name: 'NativeEvent'}, 'NonNativeEvent');

			expect(markerChild.emit).toHaveBeenCalledTimes(1);
			expect(markerChild.emit.mock.calls[0][0]).toBe('NonNativeEvent');
		});

		it('should add the normalized event data', () => {
			markerChild._handleNativeEvent({name: 'NativeEvent'}, 'NonNativeEvent');

			expect(markerChild.emit).toHaveBeenCalledTimes(1);
			expect(markerChild.emit.mock.calls[0][1]).toEqual({
				name: 'NormalizedEventData',
				nativeEvent: {name: 'NativeEvent'},
			});
		});
	});

	describe('_getNativeEventFunction', () => {
		it('returns a function', () => {
			const fn = markerChild._getNativeEventFunction('nativeEvent');

			expect(typeof fn === 'function');
		});

		it('returns a function that calls _handleNativeEvent when executed', () => {
			const fn = markerChild._getNativeEventFunction('nativeEvent');

			fn();

			expect(markerChild._handleNativeEvent).toHaveBeenCalledTimes(1);
		});

		it('returns a function that calls _handleNativeEvent with the given event object when executed', () => {
			const fn = markerChild._getNativeEventFunction('nativeEvent');

			fn({name: 'AwesomeNativeEvent'});

			expect(markerChild._handleNativeEvent).toHaveBeenCalledTimes(1);
			expect(markerChild._handleNativeEvent.mock.calls[0][0]).toEqual({
				name: 'AwesomeNativeEvent',
			});
		});

		it('returns a function that calls _handleNativeEvent with the given event type when executed', () => {
			const fn = markerChild._getNativeEventFunction('nativeEvent');

			fn();

			expect(markerChild._handleNativeEvent).toHaveBeenCalledTimes(1);
			expect(markerChild._handleNativeEvent.mock.calls[0][1]).toBe(
				'nativeEvent'
			);
		});
	});

	describe('_getNativeMarker', () => {
		it('should throw a not implemented error', () => {
			expect(
				() => {
					new MarkerBase()._getNativeMarker();
				}
			).toThrow();
		});
	});

	describe('_getNormalizedEventData', () => {
		it('should throw a not implemented error', () => {
			expect(
				() => {
					new MarkerBase()._getNormalizedEventData();
				}
			).toThrow();
		});
	});
});
