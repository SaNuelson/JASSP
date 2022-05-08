package cz.mff.jassp.option;

import cz.mff.jassp.processing.extractor.Extractor;

import java.util.Collection;

/**
 * Builder class responsible for constructing Options.
 */
public class OptionBuilder {
    private final Option builtInstance = new Option();
    private boolean isActive = true;

    OptionBuilder() {}

    /**
     * Set the option as required. It is optional by default.
     * @return this builder's updated instance
     */
    public OptionBuilder setRequired() {
        if (!isActive) {
            throw new BuilderException("Trying to use an inactive builder.");
        }
        builtInstance.required = true;
        return this;
    }

    /**
     * Set that the option should expect string parameter (unless specified otherwise
     * via {@link #setExtractor(Extractor)}) instead of being a flag.
     * @apiNote When calling {@link #setExtractor(Extractor)}, this setter is redundant.
     * @return this builder's updated instance
     */
    public OptionBuilder expectsParameter() {
        if (!isActive) {
            throw new BuilderException("Trying to use an inactive builder.");
        }

        if (builtInstance.extractor == null) {
            builtInstance.extractor = Extractor.String();
        }

        return this;
    }

    /**
     * Register new short alias to the parser.input.Option.
     * @param alias new alias to register
     * @return this builder's updated instance
     */
    public OptionBuilder addShortAlias(String alias) {
        if (!isActive) {
            throw new BuilderException("Trying to use an inactive builder.");
        }
        if (!Option.isValidShortAlias(alias)) {
            throw new BuilderException("An invalid short alias was provided.");
        }
        if (builtInstance.shortAliases.contains(alias)) {
            throw new BuilderException("An alias was provided which is already contained.");
        }

        builtInstance.shortAliases.add(alias);
        return this;
    }

    /**
     * Register new short aliases to the parser.input.Option.
     * @param aliases a collection of aliases to register
     * @return this builder's updated instance
     */
    public OptionBuilder addShortAliases(Collection<String> aliases) {
        for (String nextAlias : aliases) {
            addShortAlias(nextAlias);
        }
        return this;
    }


    /**
     * Register new long alias to the parser.input.Option.
     *
     * @param alias new alias to register
     * @return this builder's updated instance
     */
    public OptionBuilder addLongAlias(String alias) {
        if (!isActive) {
            throw new BuilderException("Trying to use an inactive builder.");
        }
        if (!Option.isValidLongAlias(alias)) {
            throw new BuilderException("An invalid long alias was provided.");
        }
        if (builtInstance.longAliases.contains(alias)) {
            throw new BuilderException("An alias was provided which is already contained.");
        }

        builtInstance.longAliases.add(alias);
        return this;
    }

    /**
     * Register new long alias to the parser.input.Option.
     *
     * @param aliases new alias to register
     * @return this builder's updated instance
     */
    public OptionBuilder addLongAliases(Collection<String> aliases) {
        for (String nextAlias : aliases) {
            addLongAlias(nextAlias);
        }
        return this;
    }

    /**
     * Set literal handler for the option.
     * If none is provided, sole long alias becomes handler.
     * If no long alias is present or multiple exist, sole short alias becomes handler.
     * If no short alias is present or multiple exist, an exception is thrown.
     *
     * @param handler value of new option handler
     * @return this builder's updated instance
     */
    public OptionBuilder setHandler(String handler) {
        if (!isActive) {
            throw new BuilderException("Trying to use an inactive builder.");
        }
        if (handler == null || handler.length() == 0) {
            throw new BuilderException("An invalid handler was provided.");
        }

        builtInstance.handler = handler;
        return this;
    }

    /**
     * Add a description for the parser.input.Option used for printing help.
     * Overwrites an existing description if one already exists.
     *
     * @param desc description of the option
     * @return this builder's updated instance
     */
    public OptionBuilder setDescription(String desc) {
        if (!isActive) {
            throw new BuilderException("Trying to use an inactive builder.");
        }

        builtInstance.description = desc;
        return this;
    }

    /**
     * Add or overwrite parser.input.Option's parser.
     * @param parser a new parser to set as parser.input.Option's parser.processing.logic.ValueParser.
     * @return this builder's updated instance
     */
    public OptionBuilder setExtractor(Extractor<?> parser) {
        if (!isActive) {
            throw new BuilderException("Trying to use an inactive builder.");
        }

        builtInstance.extractor = parser;
        return this;
    }

    /**
     * Finalize the building process and return built parser.input.Option instance.
     * Once called, the builder becomes inactive and will be locked from further usage.
     * @return new parser.input.Option if building process succeeded
     * @throws BuilderException if build failed
     */
    public Option build() throws BuilderException {
        if (!isActive) {
            throw new BuilderException("Trying to build with inactive builder.");
        }

        validateAndTryFill();
        isActive = false;
        return builtInstance;
    }

    private void validateAndTryFill() {
        // at least one alias has to be provided
        if (builtInstance.shortAliases.size() == 0 && builtInstance.longAliases.size() == 0) {
            throw new BuilderException("Trying to build option with no known aliases.");
        }

        // handler has to be set or inferred
        if (builtInstance.handler == null) {
            if (builtInstance.longAliases.size() == 1) {
                builtInstance.handler = builtInstance.longAliases.get(0);
            }
            else if (builtInstance.shortAliases.size() == 1) {
                builtInstance.handler = builtInstance.shortAliases.get(0);
            }
            else {
                throw new BuilderException("No handler was provided and it cannot be inferred.");
            }
        }

        // flag cannot be required
        if (builtInstance.extractor == null && builtInstance.required) {
            throw new BuilderException("Flag cannot be required");
        }
    }
}