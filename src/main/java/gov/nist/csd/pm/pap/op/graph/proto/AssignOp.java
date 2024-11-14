// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: graph.proto

package gov.nist.csd.pm.pap.op.graph.proto;

/**
 * Protobuf type {@code AssignOp}
 */
public final class AssignOp extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:AssignOp)
    AssignOpOrBuilder {
private static final long serialVersionUID = 0L;
  // Use AssignOp.newBuilder() to construct.
  private AssignOp(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private AssignOp() {
    ascendant_ = "";
    descendants_ = com.google.protobuf.LazyStringArrayList.EMPTY;
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new AssignOp();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private AssignOp(
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

            ascendant_ = s;
            break;
          }
          case 18: {
            java.lang.String s = input.readStringRequireUtf8();
            if (!((mutable_bitField0_ & 0x00000001) != 0)) {
              descendants_ = new com.google.protobuf.LazyStringArrayList();
              mutable_bitField0_ |= 0x00000001;
            }
            descendants_.add(s);
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
      if (((mutable_bitField0_ & 0x00000001) != 0)) {
        descendants_ = descendants_.getUnmodifiableView();
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return gov.nist.csd.pm.pap.op.graph.proto.GraphProto.internal_static_AssignOp_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return gov.nist.csd.pm.pap.op.graph.proto.GraphProto.internal_static_AssignOp_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            gov.nist.csd.pm.pap.op.graph.proto.AssignOp.class, gov.nist.csd.pm.pap.op.graph.proto.AssignOp.Builder.class);
  }

  public static final int ASCENDANT_FIELD_NUMBER = 1;
  private volatile java.lang.Object ascendant_;
  /**
   * <code>string ascendant = 1;</code>
   * @return The ascendant.
   */
  @java.lang.Override
  public java.lang.String getAscendant() {
    java.lang.Object ref = ascendant_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      ascendant_ = s;
      return s;
    }
  }
  /**
   * <code>string ascendant = 1;</code>
   * @return The bytes for ascendant.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getAscendantBytes() {
    java.lang.Object ref = ascendant_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      ascendant_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int DESCENDANTS_FIELD_NUMBER = 2;
  private com.google.protobuf.LazyStringList descendants_;
  /**
   * <code>repeated string descendants = 2;</code>
   * @return A list containing the descendants.
   */
  public com.google.protobuf.ProtocolStringList
      getDescendantsList() {
    return descendants_;
  }
  /**
   * <code>repeated string descendants = 2;</code>
   * @return The count of descendants.
   */
  public int getDescendantsCount() {
    return descendants_.size();
  }
  /**
   * <code>repeated string descendants = 2;</code>
   * @param index The index of the element to return.
   * @return The descendants at the given index.
   */
  public java.lang.String getDescendants(int index) {
    return descendants_.get(index);
  }
  /**
   * <code>repeated string descendants = 2;</code>
   * @param index The index of the value to return.
   * @return The bytes of the descendants at the given index.
   */
  public com.google.protobuf.ByteString
      getDescendantsBytes(int index) {
    return descendants_.getByteString(index);
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
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(ascendant_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, ascendant_);
    }
    for (int i = 0; i < descendants_.size(); i++) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, descendants_.getRaw(i));
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(ascendant_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, ascendant_);
    }
    {
      int dataSize = 0;
      for (int i = 0; i < descendants_.size(); i++) {
        dataSize += computeStringSizeNoTag(descendants_.getRaw(i));
      }
      size += dataSize;
      size += 1 * getDescendantsList().size();
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
    if (!(obj instanceof gov.nist.csd.pm.pap.op.graph.proto.AssignOp)) {
      return super.equals(obj);
    }
    gov.nist.csd.pm.pap.op.graph.proto.AssignOp other = (gov.nist.csd.pm.pap.op.graph.proto.AssignOp) obj;

    if (!getAscendant()
        .equals(other.getAscendant())) return false;
    if (!getDescendantsList()
        .equals(other.getDescendantsList())) return false;
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
    hash = (37 * hash) + ASCENDANT_FIELD_NUMBER;
    hash = (53 * hash) + getAscendant().hashCode();
    if (getDescendantsCount() > 0) {
      hash = (37 * hash) + DESCENDANTS_FIELD_NUMBER;
      hash = (53 * hash) + getDescendantsList().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static gov.nist.csd.pm.pap.op.graph.proto.AssignOp parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static gov.nist.csd.pm.pap.op.graph.proto.AssignOp parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static gov.nist.csd.pm.pap.op.graph.proto.AssignOp parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static gov.nist.csd.pm.pap.op.graph.proto.AssignOp parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static gov.nist.csd.pm.pap.op.graph.proto.AssignOp parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static gov.nist.csd.pm.pap.op.graph.proto.AssignOp parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static gov.nist.csd.pm.pap.op.graph.proto.AssignOp parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static gov.nist.csd.pm.pap.op.graph.proto.AssignOp parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static gov.nist.csd.pm.pap.op.graph.proto.AssignOp parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static gov.nist.csd.pm.pap.op.graph.proto.AssignOp parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static gov.nist.csd.pm.pap.op.graph.proto.AssignOp parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static gov.nist.csd.pm.pap.op.graph.proto.AssignOp parseFrom(
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
  public static Builder newBuilder(gov.nist.csd.pm.pap.op.graph.proto.AssignOp prototype) {
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
   * Protobuf type {@code AssignOp}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:AssignOp)
      gov.nist.csd.pm.pap.op.graph.proto.AssignOpOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return gov.nist.csd.pm.pap.op.graph.proto.GraphProto.internal_static_AssignOp_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return gov.nist.csd.pm.pap.op.graph.proto.GraphProto.internal_static_AssignOp_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              gov.nist.csd.pm.pap.op.graph.proto.AssignOp.class, gov.nist.csd.pm.pap.op.graph.proto.AssignOp.Builder.class);
    }

    // Construct using gov.nist.csd.pm.pap.op.graph.proto.AssignOp.newBuilder()
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
      ascendant_ = "";

      descendants_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      bitField0_ = (bitField0_ & ~0x00000001);
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return gov.nist.csd.pm.pap.op.graph.proto.GraphProto.internal_static_AssignOp_descriptor;
    }

    @java.lang.Override
    public gov.nist.csd.pm.pap.op.graph.proto.AssignOp getDefaultInstanceForType() {
      return gov.nist.csd.pm.pap.op.graph.proto.AssignOp.getDefaultInstance();
    }

    @java.lang.Override
    public gov.nist.csd.pm.pap.op.graph.proto.AssignOp build() {
      gov.nist.csd.pm.pap.op.graph.proto.AssignOp result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public gov.nist.csd.pm.pap.op.graph.proto.AssignOp buildPartial() {
      gov.nist.csd.pm.pap.op.graph.proto.AssignOp result = new gov.nist.csd.pm.pap.op.graph.proto.AssignOp(this);
      int from_bitField0_ = bitField0_;
      result.ascendant_ = ascendant_;
      if (((bitField0_ & 0x00000001) != 0)) {
        descendants_ = descendants_.getUnmodifiableView();
        bitField0_ = (bitField0_ & ~0x00000001);
      }
      result.descendants_ = descendants_;
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
      if (other instanceof gov.nist.csd.pm.pap.op.graph.proto.AssignOp) {
        return mergeFrom((gov.nist.csd.pm.pap.op.graph.proto.AssignOp)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(gov.nist.csd.pm.pap.op.graph.proto.AssignOp other) {
      if (other == gov.nist.csd.pm.pap.op.graph.proto.AssignOp.getDefaultInstance()) return this;
      if (!other.getAscendant().isEmpty()) {
        ascendant_ = other.ascendant_;
        onChanged();
      }
      if (!other.descendants_.isEmpty()) {
        if (descendants_.isEmpty()) {
          descendants_ = other.descendants_;
          bitField0_ = (bitField0_ & ~0x00000001);
        } else {
          ensureDescendantsIsMutable();
          descendants_.addAll(other.descendants_);
        }
        onChanged();
      }
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
      gov.nist.csd.pm.pap.op.graph.proto.AssignOp parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (gov.nist.csd.pm.pap.op.graph.proto.AssignOp) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private java.lang.Object ascendant_ = "";
    /**
     * <code>string ascendant = 1;</code>
     * @return The ascendant.
     */
    public java.lang.String getAscendant() {
      java.lang.Object ref = ascendant_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        ascendant_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string ascendant = 1;</code>
     * @return The bytes for ascendant.
     */
    public com.google.protobuf.ByteString
        getAscendantBytes() {
      java.lang.Object ref = ascendant_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        ascendant_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string ascendant = 1;</code>
     * @param value The ascendant to set.
     * @return This builder for chaining.
     */
    public Builder setAscendant(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      ascendant_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string ascendant = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearAscendant() {
      
      ascendant_ = getDefaultInstance().getAscendant();
      onChanged();
      return this;
    }
    /**
     * <code>string ascendant = 1;</code>
     * @param value The bytes for ascendant to set.
     * @return This builder for chaining.
     */
    public Builder setAscendantBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      ascendant_ = value;
      onChanged();
      return this;
    }

    private com.google.protobuf.LazyStringList descendants_ = com.google.protobuf.LazyStringArrayList.EMPTY;
    private void ensureDescendantsIsMutable() {
      if (!((bitField0_ & 0x00000001) != 0)) {
        descendants_ = new com.google.protobuf.LazyStringArrayList(descendants_);
        bitField0_ |= 0x00000001;
       }
    }
    /**
     * <code>repeated string descendants = 2;</code>
     * @return A list containing the descendants.
     */
    public com.google.protobuf.ProtocolStringList
        getDescendantsList() {
      return descendants_.getUnmodifiableView();
    }
    /**
     * <code>repeated string descendants = 2;</code>
     * @return The count of descendants.
     */
    public int getDescendantsCount() {
      return descendants_.size();
    }
    /**
     * <code>repeated string descendants = 2;</code>
     * @param index The index of the element to return.
     * @return The descendants at the given index.
     */
    public java.lang.String getDescendants(int index) {
      return descendants_.get(index);
    }
    /**
     * <code>repeated string descendants = 2;</code>
     * @param index The index of the value to return.
     * @return The bytes of the descendants at the given index.
     */
    public com.google.protobuf.ByteString
        getDescendantsBytes(int index) {
      return descendants_.getByteString(index);
    }
    /**
     * <code>repeated string descendants = 2;</code>
     * @param index The index to set the value at.
     * @param value The descendants to set.
     * @return This builder for chaining.
     */
    public Builder setDescendants(
        int index, java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  ensureDescendantsIsMutable();
      descendants_.set(index, value);
      onChanged();
      return this;
    }
    /**
     * <code>repeated string descendants = 2;</code>
     * @param value The descendants to add.
     * @return This builder for chaining.
     */
    public Builder addDescendants(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  ensureDescendantsIsMutable();
      descendants_.add(value);
      onChanged();
      return this;
    }
    /**
     * <code>repeated string descendants = 2;</code>
     * @param values The descendants to add.
     * @return This builder for chaining.
     */
    public Builder addAllDescendants(
        java.lang.Iterable<java.lang.String> values) {
      ensureDescendantsIsMutable();
      com.google.protobuf.AbstractMessageLite.Builder.addAll(
          values, descendants_);
      onChanged();
      return this;
    }
    /**
     * <code>repeated string descendants = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearDescendants() {
      descendants_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      bitField0_ = (bitField0_ & ~0x00000001);
      onChanged();
      return this;
    }
    /**
     * <code>repeated string descendants = 2;</code>
     * @param value The bytes of the descendants to add.
     * @return This builder for chaining.
     */
    public Builder addDescendantsBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      ensureDescendantsIsMutable();
      descendants_.add(value);
      onChanged();
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


    // @@protoc_insertion_point(builder_scope:AssignOp)
  }

  // @@protoc_insertion_point(class_scope:AssignOp)
  private static final gov.nist.csd.pm.pap.op.graph.proto.AssignOp DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new gov.nist.csd.pm.pap.op.graph.proto.AssignOp();
  }

  public static gov.nist.csd.pm.pap.op.graph.proto.AssignOp getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<AssignOp>
      PARSER = new com.google.protobuf.AbstractParser<AssignOp>() {
    @java.lang.Override
    public AssignOp parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new AssignOp(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<AssignOp> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<AssignOp> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public gov.nist.csd.pm.pap.op.graph.proto.AssignOp getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

