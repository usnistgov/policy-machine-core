// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: graph.proto

package gov.nist.csd.pm.pap.op.graph.proto;

/**
 * Protobuf type {@code SetNodePropertiesOp}
 */
public final class SetNodePropertiesOp extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:SetNodePropertiesOp)
    SetNodePropertiesOpOrBuilder {
private static final long serialVersionUID = 0L;
  // Use SetNodePropertiesOp.newBuilder() to construct.
  private SetNodePropertiesOp(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private SetNodePropertiesOp() {
    name_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new SetNodePropertiesOp();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private SetNodePropertiesOp(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 10: {
            java.lang.String s = input.readStringRequireUtf8();

            name_ = s;
            break;
          }
          case 18: {
            if (!((mutable_bitField0_ & 0x00000001) != 0)) {
              properties_ = com.google.protobuf.MapField.newMapField(
                  PropertiesDefaultEntryHolder.defaultEntry);
              mutable_bitField0_ |= 0x00000001;
            }
            com.google.protobuf.MapEntry<java.lang.String, java.lang.String>
            properties__ = input.readMessage(
                PropertiesDefaultEntryHolder.defaultEntry.getParserForType(), extensionRegistry);
            properties_.getMutableMap().put(
                properties__.getKey(), properties__.getValue());
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (com.google.protobuf.UninitializedMessageException e) {
      throw e.asInvalidProtocolBufferException().setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return gov.nist.csd.pm.pap.op.graph.proto.GraphProto.internal_static_SetNodePropertiesOp_descriptor;
  }

  @SuppressWarnings({"rawtypes"})
  @java.lang.Override
  protected com.google.protobuf.MapField internalGetMapField(
      int number) {
    switch (number) {
      case 2:
        return internalGetProperties();
      default:
        throw new RuntimeException(
            "Invalid map field number: " + number);
    }
  }
  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return gov.nist.csd.pm.pap.op.graph.proto.GraphProto.internal_static_SetNodePropertiesOp_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp.class, gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp.Builder.class);
  }

  public static final int NAME_FIELD_NUMBER = 1;
  private volatile java.lang.Object name_;
  /**
   * <code>string name = 1;</code>
   * @return The name.
   */
  @java.lang.Override
  public java.lang.String getName() {
    java.lang.Object ref = name_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      name_ = s;
      return s;
    }
  }
  /**
   * <code>string name = 1;</code>
   * @return The bytes for name.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getNameBytes() {
    java.lang.Object ref = name_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      name_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int PROPERTIES_FIELD_NUMBER = 2;
  private static final class PropertiesDefaultEntryHolder {
    static final com.google.protobuf.MapEntry<
        java.lang.String, java.lang.String> defaultEntry =
            com.google.protobuf.MapEntry
            .<java.lang.String, java.lang.String>newDefaultInstance(
                gov.nist.csd.pm.pap.op.graph.proto.GraphProto.internal_static_SetNodePropertiesOp_PropertiesEntry_descriptor, 
                com.google.protobuf.WireFormat.FieldType.STRING,
                "",
                com.google.protobuf.WireFormat.FieldType.STRING,
                "");
  }
  private com.google.protobuf.MapField<
      java.lang.String, java.lang.String> properties_;
  private com.google.protobuf.MapField<java.lang.String, java.lang.String>
  internalGetProperties() {
    if (properties_ == null) {
      return com.google.protobuf.MapField.emptyMapField(
          PropertiesDefaultEntryHolder.defaultEntry);
    }
    return properties_;
  }

  public int getPropertiesCount() {
    return internalGetProperties().getMap().size();
  }
  /**
   * <code>map&lt;string, string&gt; properties = 2;</code>
   */

  @java.lang.Override
  public boolean containsProperties(
      java.lang.String key) {
    if (key == null) { throw new NullPointerException("map key"); }
    return internalGetProperties().getMap().containsKey(key);
  }
  /**
   * Use {@link #getPropertiesMap()} instead.
   */
  @java.lang.Override
  @java.lang.Deprecated
  public java.util.Map<java.lang.String, java.lang.String> getProperties() {
    return getPropertiesMap();
  }
  /**
   * <code>map&lt;string, string&gt; properties = 2;</code>
   */
  @java.lang.Override

  public java.util.Map<java.lang.String, java.lang.String> getPropertiesMap() {
    return internalGetProperties().getMap();
  }
  /**
   * <code>map&lt;string, string&gt; properties = 2;</code>
   */
  @java.lang.Override

  public java.lang.String getPropertiesOrDefault(
      java.lang.String key,
      java.lang.String defaultValue) {
    if (key == null) { throw new NullPointerException("map key"); }
    java.util.Map<java.lang.String, java.lang.String> map =
        internalGetProperties().getMap();
    return map.containsKey(key) ? map.get(key) : defaultValue;
  }
  /**
   * <code>map&lt;string, string&gt; properties = 2;</code>
   */
  @java.lang.Override

  public java.lang.String getPropertiesOrThrow(
      java.lang.String key) {
    if (key == null) { throw new NullPointerException("map key"); }
    java.util.Map<java.lang.String, java.lang.String> map =
        internalGetProperties().getMap();
    if (!map.containsKey(key)) {
      throw new java.lang.IllegalArgumentException();
    }
    return map.get(key);
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(name_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, name_);
    }
    com.google.protobuf.GeneratedMessageV3
      .serializeStringMapTo(
        output,
        internalGetProperties(),
        PropertiesDefaultEntryHolder.defaultEntry,
        2);
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(name_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, name_);
    }
    for (java.util.Map.Entry<java.lang.String, java.lang.String> entry
         : internalGetProperties().getMap().entrySet()) {
      com.google.protobuf.MapEntry<java.lang.String, java.lang.String>
      properties__ = PropertiesDefaultEntryHolder.defaultEntry.newBuilderForType()
          .setKey(entry.getKey())
          .setValue(entry.getValue())
          .build();
      size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(2, properties__);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp)) {
      return super.equals(obj);
    }
    gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp other = (gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp) obj;

    if (!getName()
        .equals(other.getName())) return false;
    if (!internalGetProperties().equals(
        other.internalGetProperties())) return false;
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + NAME_FIELD_NUMBER;
    hash = (53 * hash) + getName().hashCode();
    if (!internalGetProperties().getMap().isEmpty()) {
      hash = (37 * hash) + PROPERTIES_FIELD_NUMBER;
      hash = (53 * hash) + internalGetProperties().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code SetNodePropertiesOp}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:SetNodePropertiesOp)
      gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOpOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return gov.nist.csd.pm.pap.op.graph.proto.GraphProto.internal_static_SetNodePropertiesOp_descriptor;
    }

    @SuppressWarnings({"rawtypes"})
    protected com.google.protobuf.MapField internalGetMapField(
        int number) {
      switch (number) {
        case 2:
          return internalGetProperties();
        default:
          throw new RuntimeException(
              "Invalid map field number: " + number);
      }
    }
    @SuppressWarnings({"rawtypes"})
    protected com.google.protobuf.MapField internalGetMutableMapField(
        int number) {
      switch (number) {
        case 2:
          return internalGetMutableProperties();
        default:
          throw new RuntimeException(
              "Invalid map field number: " + number);
      }
    }
    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return gov.nist.csd.pm.pap.op.graph.proto.GraphProto.internal_static_SetNodePropertiesOp_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp.class, gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp.Builder.class);
    }

    // Construct using gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      name_ = "";

      internalGetMutableProperties().clear();
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return gov.nist.csd.pm.pap.op.graph.proto.GraphProto.internal_static_SetNodePropertiesOp_descriptor;
    }

    @java.lang.Override
    public gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp getDefaultInstanceForType() {
      return gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp.getDefaultInstance();
    }

    @java.lang.Override
    public gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp build() {
      gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp buildPartial() {
      gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp result = new gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp(this);
      int from_bitField0_ = bitField0_;
      result.name_ = name_;
      result.properties_ = internalGetProperties();
      result.properties_.makeImmutable();
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp) {
        return mergeFrom((gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp other) {
      if (other == gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp.getDefaultInstance()) return this;
      if (!other.getName().isEmpty()) {
        name_ = other.name_;
        onChanged();
      }
      internalGetMutableProperties().mergeFrom(
          other.internalGetProperties());
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private java.lang.Object name_ = "";
    /**
     * <code>string name = 1;</code>
     * @return The name.
     */
    public java.lang.String getName() {
      java.lang.Object ref = name_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        name_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string name = 1;</code>
     * @return The bytes for name.
     */
    public com.google.protobuf.ByteString
        getNameBytes() {
      java.lang.Object ref = name_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        name_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string name = 1;</code>
     * @param value The name to set.
     * @return This builder for chaining.
     */
    public Builder setName(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      name_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string name = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearName() {
      
      name_ = getDefaultInstance().getName();
      onChanged();
      return this;
    }
    /**
     * <code>string name = 1;</code>
     * @param value The bytes for name to set.
     * @return This builder for chaining.
     */
    public Builder setNameBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      name_ = value;
      onChanged();
      return this;
    }

    private com.google.protobuf.MapField<
        java.lang.String, java.lang.String> properties_;
    private com.google.protobuf.MapField<java.lang.String, java.lang.String>
    internalGetProperties() {
      if (properties_ == null) {
        return com.google.protobuf.MapField.emptyMapField(
            PropertiesDefaultEntryHolder.defaultEntry);
      }
      return properties_;
    }
    private com.google.protobuf.MapField<java.lang.String, java.lang.String>
    internalGetMutableProperties() {
      onChanged();;
      if (properties_ == null) {
        properties_ = com.google.protobuf.MapField.newMapField(
            PropertiesDefaultEntryHolder.defaultEntry);
      }
      if (!properties_.isMutable()) {
        properties_ = properties_.copy();
      }
      return properties_;
    }

    public int getPropertiesCount() {
      return internalGetProperties().getMap().size();
    }
    /**
     * <code>map&lt;string, string&gt; properties = 2;</code>
     */

    @java.lang.Override
    public boolean containsProperties(
        java.lang.String key) {
      if (key == null) { throw new NullPointerException("map key"); }
      return internalGetProperties().getMap().containsKey(key);
    }
    /**
     * Use {@link #getPropertiesMap()} instead.
     */
    @java.lang.Override
    @java.lang.Deprecated
    public java.util.Map<java.lang.String, java.lang.String> getProperties() {
      return getPropertiesMap();
    }
    /**
     * <code>map&lt;string, string&gt; properties = 2;</code>
     */
    @java.lang.Override

    public java.util.Map<java.lang.String, java.lang.String> getPropertiesMap() {
      return internalGetProperties().getMap();
    }
    /**
     * <code>map&lt;string, string&gt; properties = 2;</code>
     */
    @java.lang.Override

    public java.lang.String getPropertiesOrDefault(
        java.lang.String key,
        java.lang.String defaultValue) {
      if (key == null) { throw new NullPointerException("map key"); }
      java.util.Map<java.lang.String, java.lang.String> map =
          internalGetProperties().getMap();
      return map.containsKey(key) ? map.get(key) : defaultValue;
    }
    /**
     * <code>map&lt;string, string&gt; properties = 2;</code>
     */
    @java.lang.Override

    public java.lang.String getPropertiesOrThrow(
        java.lang.String key) {
      if (key == null) { throw new NullPointerException("map key"); }
      java.util.Map<java.lang.String, java.lang.String> map =
          internalGetProperties().getMap();
      if (!map.containsKey(key)) {
        throw new java.lang.IllegalArgumentException();
      }
      return map.get(key);
    }

    public Builder clearProperties() {
      internalGetMutableProperties().getMutableMap()
          .clear();
      return this;
    }
    /**
     * <code>map&lt;string, string&gt; properties = 2;</code>
     */

    public Builder removeProperties(
        java.lang.String key) {
      if (key == null) { throw new NullPointerException("map key"); }
      internalGetMutableProperties().getMutableMap()
          .remove(key);
      return this;
    }
    /**
     * Use alternate mutation accessors instead.
     */
    @java.lang.Deprecated
    public java.util.Map<java.lang.String, java.lang.String>
    getMutableProperties() {
      return internalGetMutableProperties().getMutableMap();
    }
    /**
     * <code>map&lt;string, string&gt; properties = 2;</code>
     */
    public Builder putProperties(
        java.lang.String key,
        java.lang.String value) {
      if (key == null) { throw new NullPointerException("map key"); }
      if (value == null) {
  throw new NullPointerException("map value");
}

      internalGetMutableProperties().getMutableMap()
          .put(key, value);
      return this;
    }
    /**
     * <code>map&lt;string, string&gt; properties = 2;</code>
     */

    public Builder putAllProperties(
        java.util.Map<java.lang.String, java.lang.String> values) {
      internalGetMutableProperties().getMutableMap()
          .putAll(values);
      return this;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:SetNodePropertiesOp)
  }

  // @@protoc_insertion_point(class_scope:SetNodePropertiesOp)
  private static final gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp();
  }

  public static gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<SetNodePropertiesOp>
      PARSER = new com.google.protobuf.AbstractParser<SetNodePropertiesOp>() {
    @java.lang.Override
    public SetNodePropertiesOp parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new SetNodePropertiesOp(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<SetNodePropertiesOp> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<SetNodePropertiesOp> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public gov.nist.csd.pm.pap.op.graph.proto.SetNodePropertiesOp getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

