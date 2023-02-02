package growthbook.sdk.java;

/**
 * INTERNAL: Interface that is used internally for the {@link GBFeaturesRepository}
 */
interface IGBFeaturesRepository {
    void initialize() throws FeatureFetchException;

    /**
     * Required implementation to get the featuresJson
     * @return featuresJson String
     */
    String getFeaturesJson();
}
