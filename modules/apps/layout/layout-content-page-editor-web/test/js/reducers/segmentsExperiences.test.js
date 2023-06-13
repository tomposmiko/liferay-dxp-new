/* globals describe, test, jest, expect, beforeAll, afterAll */

import {createSegmentsExperienceReducer, deleteSegmentsExperienceReducer, editSegmentsExperienceReducer, selectSegmentsExperienceReducer} from '../../../src/main/resources/META-INF/resources/js/reducers/segmentsExperiences.es';
import {CREATE_SEGMENTS_EXPERIENCE, DELETE_SEGMENTS_EXPERIENCE, EDIT_SEGMENTS_EXPERIENCE, SELECT_SEGMENTS_EXPERIENCE} from '../../../src/main/resources/META-INF/resources/js/actions/actions.es';
import * as FragmentsEditorFetchUtils from '../../../src/main/resources/META-INF/resources/js/utils/FragmentsEditorFetchUtils.es';

const SEGMENTS_EXPERIENCE_ID = 'SEGMENTS_EXPERIENCE_ID';

const SEGMENTS_EXPERIENCE_ID_DEFAULT = 'SEGMENTS_EXPERIENCE_ID_DEFAULT';

const SEGMENTS_EXPERIENCE_ID_SECOND = 'SEGMENTS_EXPERIENCE_ID_SECOND';

const SEGMENTS_EXPERIENCES_LIST = [SEGMENTS_EXPERIENCE_ID, SEGMENTS_EXPERIENCE_ID_SECOND];

describe(
	'segments experiences reducers',
	() => {
		beforeEach(() => {
			jest.spyOn(FragmentsEditorFetchUtils, 'updatePageEditorLayoutData')
				.mockImplementation(() => new Promise(resolve => resolve()));
		})
		test(
			'createSegmentsExperienceReducer communicates with API and updates the state',
			done => {
				let prevLiferayGlobal = {...global.Liferay};
				let prevThemeDisplay = {...global.themeDisplay};

				global.themeDisplay = {
					getScopeGroupId() {
						return 'mockedScopeGroupId';
					},
					getUserId() {
						return 'mockedUserId';
					}
				}

				global.Liferay = {
					Service(
						URL,
						{
							active,
							classNameId,
							classPK,
							nameMap,
							segmentsEntryId
						},
						callbackFunc,
						errorFunc
					) {
						return callbackFunc(
							{
								active,
								nameCurrentValue: JSON.parse(nameMap).en_US,
								segmentsEntryId,
								segmentsExperienceId: (experiencesCount++, SEGMENTS_EXPERIENCES_LIST[experiencesCount])
							}
						);
					}
				};

				const availableSegmentsExperiences = {};
				const classNameId = 'test-class-name-id';
				const classPK = 'test-class-p-k';
				const spy = jest.spyOn(global.Liferay, 'Service');
				const defaultSegmentsExperienceId = 'DEFAULT_SEGMENTS_EXPERIENCE_ID';

				let experiencesCount = -1;

				let currentLayout = {
					'any': 'object'
				}

				const prevState = {
					availableSegmentsExperiences,
					classNameId,
					classPK,
					defaultSegmentsExperienceId,
					defaultLanguageId: 'en_US',
					layoutDataList: [{
						segmentsExperienceId: defaultSegmentsExperienceId,
						layoutData: {},
					}],
					layoutData: currentLayout
				};

				const action = {
					name: 'test experience name',
					segmentsEntryId: 'test-segment-id',
					type: CREATE_SEGMENTS_EXPERIENCE
				};

				const serviceContext = JSON.stringify({
						scopeGroupId: global.themeDisplay.getScopeGroupId(),
						userId: global.themeDisplay.getUserId()
					})

				const liferayServiceParams = {
					active: true,
					classNameId: prevState.classNameId,
					classPK: prevState.classPK,
					nameMap: JSON.stringify({en_US: action.name}),
					segmentsEntryId: action.segmentsEntryId,
					serviceContext
				};

				createSegmentsExperienceReducer(prevState, action)
				.then(response => {
					expect(response).toMatchSnapshot();

					expect(spy).toHaveBeenCalledWith(
						expect.stringContaining(''),
						liferayServiceParams,
						expect.objectContaining({}),
						expect.objectContaining({})
					);

					const secondAction = {
						name: 'second test experience name',
						segmentsEntryId: 'test-segment-id',
						type: CREATE_SEGMENTS_EXPERIENCE
					};

					const secondLiferayServiceParams = {
						active: true,
						classNameId: prevState.classNameId,
						classPK: prevState.classPK,
						nameMap: JSON.stringify({en_US: secondAction.name}),
						segmentsEntryId: secondAction.segmentsEntryId,
						serviceContext
					};

					expect(
						createSegmentsExperienceReducer(response, secondAction)
					).resolves.toMatchSnapshot();

					expect(spy).toHaveBeenLastCalledWith(
						expect.stringContaining(''),
						secondLiferayServiceParams,
						expect.objectContaining({}),
						expect.objectContaining({})
					);

					global.Liferay = prevLiferayGlobal;
					global.themeDisplay = prevThemeDisplay
					done();
				}).catch(error => { throw new Error(error); });
			}
		);

		test(
			'deleteExperience communicates with API and updates the state',
			(done) => {
				global.fetch = () => new Promise(resolve => resolve());
				global.formData = () => {};

				const spy = jest.spyOn(global, 'fetch');

				const availableSegmentsExperiences = {
					[SEGMENTS_EXPERIENCE_ID]: {
						name: 'A test experience',
						segmentsEntryId: 'notRelevantSegmentId',
						segmentsExperienceId: SEGMENTS_EXPERIENCE_ID
					},
					[SEGMENTS_EXPERIENCE_ID_DEFAULT]: {
						name: 'A default test experience',
						segmentsEntryId: 'notRelevantSegmentId',
						segmentsExperienceId: SEGMENTS_EXPERIENCE_ID_DEFAULT
					},
					[SEGMENTS_EXPERIENCE_ID_SECOND]: {
						name: 'A second test experience',
						segmentsEntryId: 'notRelevantSegmentId',
						segmentsExperienceId: SEGMENTS_EXPERIENCE_ID_SECOND
					}
				};

				const classNameId = 'test-class-name-id';
				const classPK = 'test-class-p-k';
				const deleteSegmentsExperienceURL = 'deleteSegmentsExperienceURL';

				const prevState = {
					availableSegmentsExperiences,
					classNameId,
					classPK,
					defaultLanguageId: 'en_US',
					defaultSegmentsExperienceId: SEGMENTS_EXPERIENCE_ID_DEFAULT,
					deleteSegmentsExperienceURL,
					layoutData: {
						'current': 'layout',
					},
					layoutDataList: [
						{
							segmentsExperienceId: SEGMENTS_EXPERIENCE_ID_DEFAULT,
							layoutData: {
								'default': 'layoutData'
							}
						},
						{
							segmentsExperienceId: SEGMENTS_EXPERIENCE_ID_SECOND,
							layoutData: {
								'second': 'layoutData'
							}
						},
						{
							segmentsExperienceId: SEGMENTS_EXPERIENCE_ID,
							layoutData: {
								'first': 'layoutData'
							}
						}
					],
					segmentsExperienceId: SEGMENTS_EXPERIENCE_ID_SECOND,
				};

				const fetchParams = {
					body: expect.any(Object),
					credentials: 'include',
					method: 'POST'
				};

				deleteSegmentsExperienceReducer(
					prevState,
					{
						segmentsExperienceId: SEGMENTS_EXPERIENCE_ID,
						type: DELETE_SEGMENTS_EXPERIENCE
					}
				).then(
					response => {
						expect(response).toMatchSnapshot();

						expect(spy).toHaveBeenCalledTimes(1);
						expect(spy).toHaveBeenLastCalledWith(
							expect.stringContaining(deleteSegmentsExperienceURL),
							expect.objectContaining(fetchParams)
						);

						deleteSegmentsExperienceReducer(
							response,
							{
								segmentsExperienceId: SEGMENTS_EXPERIENCE_ID_SECOND,
								type: DELETE_SEGMENTS_EXPERIENCE
							}
						).then(
							state => {
								expect(state).toMatchSnapshot();
							}
						).catch(error => { throw new Error(error); });

						expect(spy).toHaveBeenCalledTimes(2);
						expect(spy).toHaveBeenLastCalledWith(
							expect.stringContaining(deleteSegmentsExperienceURL),
							expect.objectContaining(fetchParams)
						);
						done();
						global.Liferay = prevLiferayGlobal;
					}
				).catch((error) => {
					throw new Error(error)
				});
			}
		);

		test(
			'editExperience communicates with API and updates the state',
			() => {
				let prevLiferayGlobal = {...global.Liferay};
				global.Liferay = {
					Service(
						URL,
						{
							active,
							nameMap,
							segmentsEntryId,
							segmentsExperienceId
						},
						callbackFunc,
						errorFunc
					) {
						return callbackFunc(
							{
								active,
								nameCurrentValue: JSON.parse(nameMap).en_US,
								segmentsEntryId,
								segmentsExperienceId
							}
						);
					}
				};

				const availableSegmentsExperiences = {
					[SEGMENTS_EXPERIENCE_ID]: {
						active: true,
						name: 'A test experience',
						segmentsEntryId: 'notRelevantSegmentId',
						segmentsExperienceId: SEGMENTS_EXPERIENCE_ID
					},
					[SEGMENTS_EXPERIENCE_ID_DEFAULT]: {
						active: true,
						name: 'A default test experience',
						segmentsEntryId: 'notRelevantSegmentId',
						segmentsExperienceId: SEGMENTS_EXPERIENCE_ID
					},
					[SEGMENTS_EXPERIENCE_ID_SECOND]: {
						active: true,
						name: 'A second test experience',
						segmentsEntryId: 'notRelevantSegmentId',
						segmentsExperienceId: SEGMENTS_EXPERIENCE_ID_SECOND
					}
				};

				const action = {
					name: 'A modified test experience',
					segmentsEntryId: 'relevantSegmentId',
					segmentsExperienceId: SEGMENTS_EXPERIENCE_ID,
					type: EDIT_SEGMENTS_EXPERIENCE
				};
				const prevState = {
					availableSegmentsExperiences,
					defaultLanguageId: 'en_US'
				};

				expect.assertions(3);

				editSegmentsExperienceReducer(prevState, action).then(
					state => {
						expect(state.availableSegmentsExperiences[SEGMENTS_EXPERIENCE_ID_SECOND]).toEqual(prevState.availableSegmentsExperiences[SEGMENTS_EXPERIENCE_ID_SECOND]);
						expect(state.availableSegmentsExperiences[SEGMENTS_EXPERIENCE_ID_DEFAULT]).toEqual(prevState.availableSegmentsExperiences[SEGMENTS_EXPERIENCE_ID_DEFAULT]);
						expect(state.availableSegmentsExperiences[SEGMENTS_EXPERIENCE_ID]).toEqual(
							{
								...prevState.availableSegmentsExperiences[SEGMENTS_EXPERIENCE_ID],
								name: action.name,
								segmentsEntryId: action.segmentsEntryId
							}
						);
					}
				);

				global.Liferay = prevLiferayGlobal;
			}
		);

		test('selectSegmentsExperienceReducer', done => {
			const availableSegmentsExperiences = {
				[SEGMENTS_EXPERIENCE_ID]: {
					active: true,
					name: 'A test experience',
					segmentsEntryId: 'notRelevantSegmentId',
					segmentsExperienceId: SEGMENTS_EXPERIENCE_ID
				},
				[SEGMENTS_EXPERIENCE_ID_DEFAULT]: {
					active: true,
					name: 'A default test experience',
					segmentsEntryId: 'notRelevantSegmentId',
					segmentsExperienceId: SEGMENTS_EXPERIENCE_ID_DEFAULT
				},
				[SEGMENTS_EXPERIENCE_ID_SECOND]: {
					active: true,
					name: 'A second test experience',
					segmentsEntryId: 'notRelevantSegmentId',
					segmentsExperienceId: SEGMENTS_EXPERIENCE_ID_SECOND
				}
			};
			const layoutDataList = [
				{
					segmentsExperienceId: SEGMENTS_EXPERIENCE_ID,
					layoutData: {
						testId: SEGMENTS_EXPERIENCE_ID,
					}
				}, {
					segmentsExperienceId: SEGMENTS_EXPERIENCE_ID_DEFAULT,
					layoutData: {
						testId: SEGMENTS_EXPERIENCE_ID_DEFAULT,
					}
				}, {
					segmentsExperienceId: SEGMENTS_EXPERIENCE_ID_SECOND,
					layoutData: {
						testId: SEGMENTS_EXPERIENCE_ID_SECOND
					}
				}
			]
			const prevState = {
				availableSegmentsExperiences,
				layoutDataList,
				segmentsExperienceId: SEGMENTS_EXPERIENCE_ID_DEFAULT,
				layoutData: {
					testId: `switched-${SEGMENTS_EXPERIENCE_ID_DEFAULT}`
				}
			};

			const prevAction = {
				segmentsExperienceId: SEGMENTS_EXPERIENCE_ID_SECOND,
				type: SELECT_SEGMENTS_EXPERIENCE
			};

			selectSegmentsExperienceReducer(prevState, prevAction)
				.then(response => {
					expect(response).toMatchSnapshot()
					done();
				}).catch(error => {
					throw new Error(error);
				})
		})

		afterEach(() => {
			jest.restoreAllMocks()
		})
	}
);