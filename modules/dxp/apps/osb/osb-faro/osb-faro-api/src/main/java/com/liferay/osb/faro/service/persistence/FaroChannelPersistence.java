/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.faro.service.persistence;

import com.liferay.osb.faro.exception.NoSuchFaroChannelException;
import com.liferay.osb.faro.model.FaroChannel;
import com.liferay.portal.kernel.service.persistence.BasePersistence;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The persistence interface for the faro channel service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Matthew Kong
 * @see FaroChannelUtil
 * @generated
 */
@ProviderType
public interface FaroChannelPersistence extends BasePersistence<FaroChannel> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link FaroChannelUtil} to access the faro channel persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	 * Returns all the faro channels where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the matching faro channels
	 */
	public java.util.List<FaroChannel> findByGroupId(long groupId);

	/**
	 * Returns a range of all the faro channels where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>FaroChannelModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of faro channels
	 * @param end the upper bound of the range of faro channels (not inclusive)
	 * @return the range of matching faro channels
	 */
	public java.util.List<FaroChannel> findByGroupId(
		long groupId, int start, int end);

	/**
	 * Returns an ordered range of all the faro channels where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>FaroChannelModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of faro channels
	 * @param end the upper bound of the range of faro channels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching faro channels
	 */
	public java.util.List<FaroChannel> findByGroupId(
		long groupId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<FaroChannel>
			orderByComparator);

	/**
	 * Returns an ordered range of all the faro channels where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>FaroChannelModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of faro channels
	 * @param end the upper bound of the range of faro channels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching faro channels
	 */
	public java.util.List<FaroChannel> findByGroupId(
		long groupId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<FaroChannel>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first faro channel in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching faro channel
	 * @throws NoSuchFaroChannelException if a matching faro channel could not be found
	 */
	public FaroChannel findByGroupId_First(
			long groupId,
			com.liferay.portal.kernel.util.OrderByComparator<FaroChannel>
				orderByComparator)
		throws NoSuchFaroChannelException;

	/**
	 * Returns the first faro channel in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching faro channel, or <code>null</code> if a matching faro channel could not be found
	 */
	public FaroChannel fetchByGroupId_First(
		long groupId,
		com.liferay.portal.kernel.util.OrderByComparator<FaroChannel>
			orderByComparator);

	/**
	 * Returns the last faro channel in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching faro channel
	 * @throws NoSuchFaroChannelException if a matching faro channel could not be found
	 */
	public FaroChannel findByGroupId_Last(
			long groupId,
			com.liferay.portal.kernel.util.OrderByComparator<FaroChannel>
				orderByComparator)
		throws NoSuchFaroChannelException;

	/**
	 * Returns the last faro channel in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching faro channel, or <code>null</code> if a matching faro channel could not be found
	 */
	public FaroChannel fetchByGroupId_Last(
		long groupId,
		com.liferay.portal.kernel.util.OrderByComparator<FaroChannel>
			orderByComparator);

	/**
	 * Returns the faro channels before and after the current faro channel in the ordered set where groupId = &#63;.
	 *
	 * @param faroChannelId the primary key of the current faro channel
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next faro channel
	 * @throws NoSuchFaroChannelException if a faro channel with the primary key could not be found
	 */
	public FaroChannel[] findByGroupId_PrevAndNext(
			long faroChannelId, long groupId,
			com.liferay.portal.kernel.util.OrderByComparator<FaroChannel>
				orderByComparator)
		throws NoSuchFaroChannelException;

	/**
	 * Removes all the faro channels where groupId = &#63; from the database.
	 *
	 * @param groupId the group ID
	 */
	public void removeByGroupId(long groupId);

	/**
	 * Returns the number of faro channels where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the number of matching faro channels
	 */
	public int countByGroupId(long groupId);

	/**
	 * Returns all the faro channels where workspaceGroupId = &#63;.
	 *
	 * @param workspaceGroupId the workspace group ID
	 * @return the matching faro channels
	 */
	public java.util.List<FaroChannel> findByWorkspaceGroupId(
		long workspaceGroupId);

	/**
	 * Returns a range of all the faro channels where workspaceGroupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>FaroChannelModelImpl</code>.
	 * </p>
	 *
	 * @param workspaceGroupId the workspace group ID
	 * @param start the lower bound of the range of faro channels
	 * @param end the upper bound of the range of faro channels (not inclusive)
	 * @return the range of matching faro channels
	 */
	public java.util.List<FaroChannel> findByWorkspaceGroupId(
		long workspaceGroupId, int start, int end);

	/**
	 * Returns an ordered range of all the faro channels where workspaceGroupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>FaroChannelModelImpl</code>.
	 * </p>
	 *
	 * @param workspaceGroupId the workspace group ID
	 * @param start the lower bound of the range of faro channels
	 * @param end the upper bound of the range of faro channels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching faro channels
	 */
	public java.util.List<FaroChannel> findByWorkspaceGroupId(
		long workspaceGroupId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<FaroChannel>
			orderByComparator);

	/**
	 * Returns an ordered range of all the faro channels where workspaceGroupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>FaroChannelModelImpl</code>.
	 * </p>
	 *
	 * @param workspaceGroupId the workspace group ID
	 * @param start the lower bound of the range of faro channels
	 * @param end the upper bound of the range of faro channels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching faro channels
	 */
	public java.util.List<FaroChannel> findByWorkspaceGroupId(
		long workspaceGroupId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<FaroChannel>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first faro channel in the ordered set where workspaceGroupId = &#63;.
	 *
	 * @param workspaceGroupId the workspace group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching faro channel
	 * @throws NoSuchFaroChannelException if a matching faro channel could not be found
	 */
	public FaroChannel findByWorkspaceGroupId_First(
			long workspaceGroupId,
			com.liferay.portal.kernel.util.OrderByComparator<FaroChannel>
				orderByComparator)
		throws NoSuchFaroChannelException;

	/**
	 * Returns the first faro channel in the ordered set where workspaceGroupId = &#63;.
	 *
	 * @param workspaceGroupId the workspace group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching faro channel, or <code>null</code> if a matching faro channel could not be found
	 */
	public FaroChannel fetchByWorkspaceGroupId_First(
		long workspaceGroupId,
		com.liferay.portal.kernel.util.OrderByComparator<FaroChannel>
			orderByComparator);

	/**
	 * Returns the last faro channel in the ordered set where workspaceGroupId = &#63;.
	 *
	 * @param workspaceGroupId the workspace group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching faro channel
	 * @throws NoSuchFaroChannelException if a matching faro channel could not be found
	 */
	public FaroChannel findByWorkspaceGroupId_Last(
			long workspaceGroupId,
			com.liferay.portal.kernel.util.OrderByComparator<FaroChannel>
				orderByComparator)
		throws NoSuchFaroChannelException;

	/**
	 * Returns the last faro channel in the ordered set where workspaceGroupId = &#63;.
	 *
	 * @param workspaceGroupId the workspace group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching faro channel, or <code>null</code> if a matching faro channel could not be found
	 */
	public FaroChannel fetchByWorkspaceGroupId_Last(
		long workspaceGroupId,
		com.liferay.portal.kernel.util.OrderByComparator<FaroChannel>
			orderByComparator);

	/**
	 * Returns the faro channels before and after the current faro channel in the ordered set where workspaceGroupId = &#63;.
	 *
	 * @param faroChannelId the primary key of the current faro channel
	 * @param workspaceGroupId the workspace group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next faro channel
	 * @throws NoSuchFaroChannelException if a faro channel with the primary key could not be found
	 */
	public FaroChannel[] findByWorkspaceGroupId_PrevAndNext(
			long faroChannelId, long workspaceGroupId,
			com.liferay.portal.kernel.util.OrderByComparator<FaroChannel>
				orderByComparator)
		throws NoSuchFaroChannelException;

	/**
	 * Removes all the faro channels where workspaceGroupId = &#63; from the database.
	 *
	 * @param workspaceGroupId the workspace group ID
	 */
	public void removeByWorkspaceGroupId(long workspaceGroupId);

	/**
	 * Returns the number of faro channels where workspaceGroupId = &#63;.
	 *
	 * @param workspaceGroupId the workspace group ID
	 * @return the number of matching faro channels
	 */
	public int countByWorkspaceGroupId(long workspaceGroupId);

	/**
	 * Returns all the faro channels where groupId = &#63; and userId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param userId the user ID
	 * @return the matching faro channels
	 */
	public java.util.List<FaroChannel> findByG_U(long groupId, long userId);

	/**
	 * Returns a range of all the faro channels where groupId = &#63; and userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>FaroChannelModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param userId the user ID
	 * @param start the lower bound of the range of faro channels
	 * @param end the upper bound of the range of faro channels (not inclusive)
	 * @return the range of matching faro channels
	 */
	public java.util.List<FaroChannel> findByG_U(
		long groupId, long userId, int start, int end);

	/**
	 * Returns an ordered range of all the faro channels where groupId = &#63; and userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>FaroChannelModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param userId the user ID
	 * @param start the lower bound of the range of faro channels
	 * @param end the upper bound of the range of faro channels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching faro channels
	 */
	public java.util.List<FaroChannel> findByG_U(
		long groupId, long userId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<FaroChannel>
			orderByComparator);

	/**
	 * Returns an ordered range of all the faro channels where groupId = &#63; and userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>FaroChannelModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param userId the user ID
	 * @param start the lower bound of the range of faro channels
	 * @param end the upper bound of the range of faro channels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching faro channels
	 */
	public java.util.List<FaroChannel> findByG_U(
		long groupId, long userId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<FaroChannel>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first faro channel in the ordered set where groupId = &#63; and userId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching faro channel
	 * @throws NoSuchFaroChannelException if a matching faro channel could not be found
	 */
	public FaroChannel findByG_U_First(
			long groupId, long userId,
			com.liferay.portal.kernel.util.OrderByComparator<FaroChannel>
				orderByComparator)
		throws NoSuchFaroChannelException;

	/**
	 * Returns the first faro channel in the ordered set where groupId = &#63; and userId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching faro channel, or <code>null</code> if a matching faro channel could not be found
	 */
	public FaroChannel fetchByG_U_First(
		long groupId, long userId,
		com.liferay.portal.kernel.util.OrderByComparator<FaroChannel>
			orderByComparator);

	/**
	 * Returns the last faro channel in the ordered set where groupId = &#63; and userId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching faro channel
	 * @throws NoSuchFaroChannelException if a matching faro channel could not be found
	 */
	public FaroChannel findByG_U_Last(
			long groupId, long userId,
			com.liferay.portal.kernel.util.OrderByComparator<FaroChannel>
				orderByComparator)
		throws NoSuchFaroChannelException;

	/**
	 * Returns the last faro channel in the ordered set where groupId = &#63; and userId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching faro channel, or <code>null</code> if a matching faro channel could not be found
	 */
	public FaroChannel fetchByG_U_Last(
		long groupId, long userId,
		com.liferay.portal.kernel.util.OrderByComparator<FaroChannel>
			orderByComparator);

	/**
	 * Returns the faro channels before and after the current faro channel in the ordered set where groupId = &#63; and userId = &#63;.
	 *
	 * @param faroChannelId the primary key of the current faro channel
	 * @param groupId the group ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next faro channel
	 * @throws NoSuchFaroChannelException if a faro channel with the primary key could not be found
	 */
	public FaroChannel[] findByG_U_PrevAndNext(
			long faroChannelId, long groupId, long userId,
			com.liferay.portal.kernel.util.OrderByComparator<FaroChannel>
				orderByComparator)
		throws NoSuchFaroChannelException;

	/**
	 * Removes all the faro channels where groupId = &#63; and userId = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param userId the user ID
	 */
	public void removeByG_U(long groupId, long userId);

	/**
	 * Returns the number of faro channels where groupId = &#63; and userId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param userId the user ID
	 * @return the number of matching faro channels
	 */
	public int countByG_U(long groupId, long userId);

	/**
	 * Returns the faro channel where channelId = &#63; and workspaceGroupId = &#63; or throws a <code>NoSuchFaroChannelException</code> if it could not be found.
	 *
	 * @param channelId the channel ID
	 * @param workspaceGroupId the workspace group ID
	 * @return the matching faro channel
	 * @throws NoSuchFaroChannelException if a matching faro channel could not be found
	 */
	public FaroChannel findByChannelId(String channelId, long workspaceGroupId)
		throws NoSuchFaroChannelException;

	/**
	 * Returns the faro channel where channelId = &#63; and workspaceGroupId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param channelId the channel ID
	 * @param workspaceGroupId the workspace group ID
	 * @return the matching faro channel, or <code>null</code> if a matching faro channel could not be found
	 */
	public FaroChannel fetchByChannelId(
		String channelId, long workspaceGroupId);

	/**
	 * Returns the faro channel where channelId = &#63; and workspaceGroupId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param channelId the channel ID
	 * @param workspaceGroupId the workspace group ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching faro channel, or <code>null</code> if a matching faro channel could not be found
	 */
	public FaroChannel fetchByChannelId(
		String channelId, long workspaceGroupId, boolean useFinderCache);

	/**
	 * Removes the faro channel where channelId = &#63; and workspaceGroupId = &#63; from the database.
	 *
	 * @param channelId the channel ID
	 * @param workspaceGroupId the workspace group ID
	 * @return the faro channel that was removed
	 */
	public FaroChannel removeByChannelId(
			String channelId, long workspaceGroupId)
		throws NoSuchFaroChannelException;

	/**
	 * Returns the number of faro channels where channelId = &#63; and workspaceGroupId = &#63;.
	 *
	 * @param channelId the channel ID
	 * @param workspaceGroupId the workspace group ID
	 * @return the number of matching faro channels
	 */
	public int countByChannelId(String channelId, long workspaceGroupId);

	/**
	 * Caches the faro channel in the entity cache if it is enabled.
	 *
	 * @param faroChannel the faro channel
	 */
	public void cacheResult(FaroChannel faroChannel);

	/**
	 * Caches the faro channels in the entity cache if it is enabled.
	 *
	 * @param faroChannels the faro channels
	 */
	public void cacheResult(java.util.List<FaroChannel> faroChannels);

	/**
	 * Creates a new faro channel with the primary key. Does not add the faro channel to the database.
	 *
	 * @param faroChannelId the primary key for the new faro channel
	 * @return the new faro channel
	 */
	public FaroChannel create(long faroChannelId);

	/**
	 * Removes the faro channel with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param faroChannelId the primary key of the faro channel
	 * @return the faro channel that was removed
	 * @throws NoSuchFaroChannelException if a faro channel with the primary key could not be found
	 */
	public FaroChannel remove(long faroChannelId)
		throws NoSuchFaroChannelException;

	public FaroChannel updateImpl(FaroChannel faroChannel);

	/**
	 * Returns the faro channel with the primary key or throws a <code>NoSuchFaroChannelException</code> if it could not be found.
	 *
	 * @param faroChannelId the primary key of the faro channel
	 * @return the faro channel
	 * @throws NoSuchFaroChannelException if a faro channel with the primary key could not be found
	 */
	public FaroChannel findByPrimaryKey(long faroChannelId)
		throws NoSuchFaroChannelException;

	/**
	 * Returns the faro channel with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param faroChannelId the primary key of the faro channel
	 * @return the faro channel, or <code>null</code> if a faro channel with the primary key could not be found
	 */
	public FaroChannel fetchByPrimaryKey(long faroChannelId);

	/**
	 * Returns all the faro channels.
	 *
	 * @return the faro channels
	 */
	public java.util.List<FaroChannel> findAll();

	/**
	 * Returns a range of all the faro channels.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>FaroChannelModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of faro channels
	 * @param end the upper bound of the range of faro channels (not inclusive)
	 * @return the range of faro channels
	 */
	public java.util.List<FaroChannel> findAll(int start, int end);

	/**
	 * Returns an ordered range of all the faro channels.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>FaroChannelModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of faro channels
	 * @param end the upper bound of the range of faro channels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of faro channels
	 */
	public java.util.List<FaroChannel> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<FaroChannel>
			orderByComparator);

	/**
	 * Returns an ordered range of all the faro channels.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>FaroChannelModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of faro channels
	 * @param end the upper bound of the range of faro channels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of faro channels
	 */
	public java.util.List<FaroChannel> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<FaroChannel>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Removes all the faro channels from the database.
	 */
	public void removeAll();

	/**
	 * Returns the number of faro channels.
	 *
	 * @return the number of faro channels
	 */
	public int countAll();

}