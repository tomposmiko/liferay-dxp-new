/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.search.test.util.aggregation.pipeline;

import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.search.aggregation.AggregationResult;
import com.liferay.portal.search.aggregation.bucket.Bucket;
import com.liferay.portal.search.aggregation.bucket.HistogramAggregation;
import com.liferay.portal.search.aggregation.bucket.HistogramAggregationResult;
import com.liferay.portal.search.aggregation.metrics.SumAggregation;
import com.liferay.portal.search.aggregation.pipeline.DerivativePipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.DerivativePipelineAggregationResult;
import com.liferay.portal.search.test.util.indexing.BaseIndexingTestCase;
import com.liferay.portal.search.test.util.indexing.DocumentCreationHelpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Michael C. Han
 */
public abstract class BaseDerivativePipelineAggregationTestCase
	extends BaseIndexingTestCase {

	@Test
	public void testDerivative() throws Exception {
		for (int i = 1; i <= 20; i++) {
			addDocument(
				DocumentCreationHelpers.singleNumber(Field.PRIORITY, i));
		}

		HistogramAggregation histogramAggregation = aggregations.histogram(
			"histogram", Field.PRIORITY);

		histogramAggregation.setInterval(5.0);
		histogramAggregation.setMinDocCount(1L);

		SumAggregation sumAggregation = aggregations.sum("sum", Field.PRIORITY);

		histogramAggregation.addChildAggregation(sumAggregation);

		DerivativePipelineAggregation derivativePipelineAggregation =
			aggregations.derivative("derivative", "sum");

		histogramAggregation.addPipelineAggregation(
			derivativePipelineAggregation);

		assertSearch(
			indexingTestHelper -> {
				indexingTestHelper.defineRequest(
					searchRequestBuilder -> searchRequestBuilder.addAggregation(
						histogramAggregation));

				indexingTestHelper.search();

				HistogramAggregationResult histogramAggregationResult =
					indexingTestHelper.getAggregationResult(
						histogramAggregation);

				List<Bucket> buckets = new ArrayList<>(
					histogramAggregationResult.getBuckets());

				Assert.assertEquals("Number of buckets", 5, buckets.size());

				assertBucket(buckets.get(0), "0.0", 4, null);
				assertBucket(buckets.get(1), "5.0", 5, 25.0);
				assertBucket(buckets.get(2), "10.0", 5, 25.0);
				assertBucket(buckets.get(3), "15.0", 5, 25.0);
				assertBucket(buckets.get(4), "20.0", 1, -65.0);
			});
	}

	protected void assertBucket(
		Bucket bucket, String expectedKey, long expectedCount,
		Double derivativeValue) {

		Assert.assertEquals(expectedKey, bucket.getKey());
		Assert.assertEquals(expectedCount, bucket.getDocCount());

		Map<String, AggregationResult> childrenAggregationResults =
			bucket.getChildrenAggregationResults();

		DerivativePipelineAggregationResult
			derivativePipelineAggregationResult =
				(DerivativePipelineAggregationResult)
					childrenAggregationResults.get("derivative");

		if (derivativeValue != null) {
			Assert.assertNotNull(derivativePipelineAggregationResult);

			Assert.assertEquals(
				"Derivative value", derivativeValue,
				derivativePipelineAggregationResult.getNormalizedValue(), 0);
		}
	}

}