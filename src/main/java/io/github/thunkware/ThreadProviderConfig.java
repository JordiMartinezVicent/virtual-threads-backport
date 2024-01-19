package io.github.thunkware;

import java.util.EnumMap;
import java.util.Map;

import static io.github.thunkware.CompatibilityPolicy.THROW_EXCEPTION;
import static io.github.thunkware.ThreadProviderFactory.isJava21;

/**
 * Configuration for {@link ThreadProvider}
 */
public class ThreadProviderConfig {

    private final Map<ThreadFeature, CompatibilityPolicy> policies = new EnumMap<>(ThreadFeature.class);

    /**
     * Same as setCompatibilityPolicy(ThreadFeature, THROW_EXCEPTION)
     * @param threadFeature ThreadFeature
     * @return this
     */
    public ThreadProviderConfig throwExceptionWhen(ThreadFeature threadFeature) {
        return setCompatibilityPolicy(threadFeature, CompatibilityPolicy.THROW_EXCEPTION);
    }

    /**
     * Get CompatibilityPolicy for a thread feature
     * @param threadFeature ThreadFeature
     * @return CompatibilityPolicy
     */
    public CompatibilityPolicy getCompatibilityPolicy(ThreadFeature threadFeature) {
        return policies.getOrDefault(threadFeature, CompatibilityPolicy.BEST_EFFORT);
    }
    
    /**
     * Sets CompatibilityPolicy for a thread feature
     * @param threadFeature ThreadFeature
     * @param policy CompatibilityPolicy
     * @return this
     */
    public ThreadProviderConfig setCompatibilityPolicy(ThreadFeature threadFeature, CompatibilityPolicy policy) {
        policies.put(threadFeature, policy);
        return this;
    }

    void reset() {
        policies.clear();
    }

    void enforceCompatibilityPolicy(ThreadFeature threadFeature) {
        if (!isJava21()) {
            CompatibilityPolicy compatibilityPolicy = getCompatibilityPolicy(threadFeature);
            if (compatibilityPolicy == THROW_EXCEPTION) {
                throw new IncompatibilityException("Java21 Virtual Threads feature unexpectedly accessed in Java8+ VM");
            }
        }
    }
}
