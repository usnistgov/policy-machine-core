// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: graph.proto

package gov.nist.csd.pm.pap.op.graph.proto;

/**
 * Protobuf type {@code AssociateOp}
 */
public final class AssociateOp extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:AssociateOp)
    AssociateOpOrBuilder {
private static final long serialVersionUID = 0L;
  // Use AssociateOp.newBuilder() to construct.
  private AssociateOp(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private AssociateOp() {
    ua_ = "";
    target_ = "";
    arset_ = com.google.protobuf.LazyStringArrayList.EMPTY;
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new AssociateOp();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private AssociateOp(
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

            ua_ = s;
            break;
          }
          case 18: {
            java.lang.String s = input.readStringRequireUtf8();

            target_ = s;
            break;
          }
          case 26: {
            java.lang.String s = input.readStringRequireUtf8();
            if (!((mutable_bitField0_ & 0x00000001) != 0)) {
              arset_ = new com.google.protobuf.LazyStringArrayList();
              mutable_bitField0_ |= 0x00000001;
            }
            arset_.add(s);
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
        arset_ = arset_.getUnmodifiableView();
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return gov.nist.csd.pm.pap.op.graph.proto.GraphProto.internal_static_AssociateOp_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return gov.nist.csd.pm.pap.op.graph.proto.GraphProto.internal_static_AssociateOp_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            gov.nist.csd.pm.pap.op.graph.proto.AssociateOp.class, gov.nist.csd.pm.pap.op.graph.proto.AssociateOp.Builder.class);
  }

  public static final int UA_FIELD_NUMBER = 1;
  private volatile java.lang.Object ua_;
  /**
   * <code>string ua = 1;</code>
   * @return The ua.
   */
  @java.lang.Override
  public java.lang.String getUa() {
    java.lang.Object ref = ua_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      ua_ = s;
      return s;
    }
  }
  /**
   * <code>string ua = 1;</code>
   * @return The bytes for ua.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getUaBytes() {
    java.lang.Object ref = ua_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      ua_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int TARGET_FIELD_NUMBER = 2;
  private volatile java.lang.Object target_;
  /**
   * <code>string target = 2;</code>
   * @return The target.
   */
  @java.lang.Override
  public java.lang.String getTarget() {
    java.lang.Object ref = target_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      target_ = s;
      return s;
    }
  }
  /**
   * <code>string target = 2;</code>
   * @return The bytes for target.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getTargetBytes() {
    java.lang.Object ref = target_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      target_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int ARSET_FIELD_NUMBER = 3;
  private com.google.protobuf.LazyStringList arset_;
  /**
   * <code>repeated string arset = 3;</code>
   * @return A list containing the arset.
   */
  public com.google.protobuf.ProtocolStringList
      getArsetList() {
    return arset_;
  }
  /**
   * <code>repeated string arset = 3;</code>
   * @return The count of arset.
   */
  public int getArsetCount() {
    return arset_.size();
  }
  /**
   * <code>repeated string arset = 3;</code>
   * @param index The index of the element to return.
   * @return The arset at the given index.
   */
  public java.lang.String getArset(int index) {
    return arset_.get(index);
  }
  /**
   * <code>repeated string arset = 3;</code>
   * @param index The index of the value to return.
   * @return The bytes of the arset at the given index.
   */
  public com.google.protobuf.ByteString
      getArsetBytes(int index) {
    return arset_.getByteString(index);
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
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(ua_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, ua_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(target_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, target_);
    }
    for (int i = 0; i < arset_.size(); i++) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 3, arset_.getRaw(i));
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(ua_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, ua_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(target_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, target_);
    }
    {
      int dataSize = 0;
      for (int i = 0; i < arset_.size(); i++) {
        dataSize += computeStringSizeNoTag(arset_.getRaw(i));
      }
      size += dataSize;
      size += 1 * getArsetList().size();
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
    if (!(obj instanceof gov.nist.csd.pm.pap.op.graph.proto.AssociateOp)) {
      return super.equals(obj);
    }
    gov.nist.csd.pm.pap.op.graph.proto.AssociateOp other = (gov.nist.csd.pm.pap.op.graph.proto.AssociateOp) obj;

    if (!getUa()
        .equals(other.getUa())) return false;
    if (!getTarget()
        .equals(other.getTarget())) return false;
    if (!getArsetList()
        .equals(other.getArsetList())) return false;
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
    hash = (37 * hash) + UA_FIELD_NUMBER;
    hash = (53 * hash) + getUa().hashCode();
    hash = (37 * hash) + TARGET_FIELD_NUMBER;
    hash = (53 * hash) + getTarget().hashCode();
    if (getArsetCount() > 0) {
      hash = (37 * hash) + ARSET_FIELD_NUMBER;
      hash = (53 * hash) + getArsetList().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static gov.nist.csd.pm.pap.op.graph.proto.AssociateOp parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static gov.nist.csd.pm.pap.op.graph.proto.AssociateOp parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static gov.nist.csd.pm.pap.op.graph.proto.AssociateOp parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static gov.nist.csd.pm.pap.op.graph.proto.AssociateOp parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static gov.nist.csd.pm.pap.op.graph.proto.AssociateOp parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static gov.nist.csd.pm.pap.op.graph.proto.AssociateOp parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static gov.nist.csd.pm.pap.op.graph.proto.AssociateOp parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static gov.nist.csd.pm.pap.op.graph.proto.AssociateOp parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static gov.nist.csd.pm.pap.op.graph.proto.AssociateOp parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static gov.nist.csd.pm.pap.op.graph.proto.AssociateOp parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static gov.nist.csd.pm.pap.op.graph.proto.AssociateOp parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static gov.nist.csd.pm.pap.op.graph.proto.AssociateOp parseFrom(
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
  public static Builder newBuilder(gov.nist.csd.pm.pap.op.graph.proto.AssociateOp prototype) {
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
   * Protobuf type {@code AssociateOp}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:AssociateOp)
      gov.nist.csd.pm.pap.op.graph.proto.AssociateOpOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return gov.nist.csd.pm.pap.op.graph.proto.GraphProto.internal_static_AssociateOp_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return gov.nist.csd.pm.pap.op.graph.proto.GraphProto.internal_static_AssociateOp_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              gov.nist.csd.pm.pap.op.graph.proto.AssociateOp.class, gov.nist.csd.pm.pap.op.graph.proto.AssociateOp.Builder.class);
    }

    // Construct using gov.nist.csd.pm.pap.op.graph.proto.AssociateOp.newBuilder()
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
      ua_ = "";

      target_ = "";

      arset_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      bitField0_ = (bitField0_ & ~0x00000001);
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return gov.nist.csd.pm.pap.op.graph.proto.GraphProto.internal_static_AssociateOp_descriptor;
    }

    @java.lang.Override
    public gov.nist.csd.pm.pap.op.graph.proto.AssociateOp getDefaultInstanceForType() {
      return gov.nist.csd.pm.pap.op.graph.proto.AssociateOp.getDefaultInstance();
    }

    @java.lang.Override
    public gov.nist.csd.pm.pap.op.graph.proto.AssociateOp build() {
      gov.nist.csd.pm.pap.op.graph.proto.AssociateOp result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public gov.nist.csd.pm.pap.op.graph.proto.AssociateOp buildPartial() {
      gov.nist.csd.pm.pap.op.graph.proto.AssociateOp result = new gov.nist.csd.pm.pap.op.graph.proto.AssociateOp(this);
      int from_bitField0_ = bitField0_;
      result.ua_ = ua_;
      result.target_ = target_;
      if (((bitField0_ & 0x00000001) != 0)) {
        arset_ = arset_.getUnmodifiableView();
        bitField0_ = (bitField0_ & ~0x00000001);
      }
      result.arset_ = arset_;
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
      if (other instanceof gov.nist.csd.pm.pap.op.graph.proto.AssociateOp) {
        return mergeFrom((gov.nist.csd.pm.pap.op.graph.proto.AssociateOp)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(gov.nist.csd.pm.pap.op.graph.proto.AssociateOp other) {
      if (other == gov.nist.csd.pm.pap.op.graph.proto.AssociateOp.getDefaultInstance()) return this;
      if (!other.getUa().isEmpty()) {
        ua_ = other.ua_;
        onChanged();
      }
      if (!other.getTarget().isEmpty()) {
        target_ = other.target_;
        onChanged();
      }
      if (!other.arset_.isEmpty()) {
        if (arset_.isEmpty()) {
          arset_ = other.arset_;
          bitField0_ = (bitField0_ & ~0x00000001);
        } else {
          ensureArsetIsMutable();
          arset_.addAll(other.arset_);
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
      gov.nist.csd.pm.pap.op.graph.proto.AssociateOp parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (gov.nist.csd.pm.pap.op.graph.proto.AssociateOp) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private java.lang.Object ua_ = "";
    /**
     * <code>string ua = 1;</code>
     * @return The ua.
     */
    public java.lang.String getUa() {
      java.lang.Object ref = ua_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        ua_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string ua = 1;</code>
     * @return The bytes for ua.
     */
    public com.google.protobuf.ByteString
        getUaBytes() {
      java.lang.Object ref = ua_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        ua_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string ua = 1;</code>
     * @param value The ua to set.
     * @return This builder for chaining.
     */
    public Builder setUa(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      ua_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string ua = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearUa() {
      
      ua_ = getDefaultInstance().getUa();
      onChanged();
      return this;
    }
    /**
     * <code>string ua = 1;</code>
     * @param value The bytes for ua to set.
     * @return This builder for chaining.
     */
    public Builder setUaBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      ua_ = value;
      onChanged();
      return this;
    }

    private java.lang.Object target_ = "";
    /**
     * <code>string target = 2;</code>
     * @return The target.
     */
    public java.lang.String getTarget() {
      java.lang.Object ref = target_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        target_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string target = 2;</code>
     * @return The bytes for target.
     */
    public com.google.protobuf.ByteString
        getTargetBytes() {
      java.lang.Object ref = target_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        target_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string target = 2;</code>
     * @param value The target to set.
     * @return This builder for chaining.
     */
    public Builder setTarget(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      target_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string target = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearTarget() {
      
      target_ = getDefaultInstance().getTarget();
      onChanged();
      return this;
    }
    /**
     * <code>string target = 2;</code>
     * @param value The bytes for target to set.
     * @return This builder for chaining.
     */
    public Builder setTargetBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      target_ = value;
      onChanged();
      return this;
    }

    private com.google.protobuf.LazyStringList arset_ = com.google.protobuf.LazyStringArrayList.EMPTY;
    private void ensureArsetIsMutable() {
      if (!((bitField0_ & 0x00000001) != 0)) {
        arset_ = new com.google.protobuf.LazyStringArrayList(arset_);
        bitField0_ |= 0x00000001;
       }
    }
    /**
     * <code>repeated string arset = 3;</code>
     * @return A list containing the arset.
     */
    public com.google.protobuf.ProtocolStringList
        getArsetList() {
      return arset_.getUnmodifiableView();
    }
    /**
     * <code>repeated string arset = 3;</code>
     * @return The count of arset.
     */
    public int getArsetCount() {
      return arset_.size();
    }
    /**
     * <code>repeated string arset = 3;</code>
     * @param index The index of the element to return.
     * @return The arset at the given index.
     */
    public java.lang.String getArset(int index) {
      return arset_.get(index);
    }
    /**
     * <code>repeated string arset = 3;</code>
     * @param index The index of the value to return.
     * @return The bytes of the arset at the given index.
     */
    public com.google.protobuf.ByteString
        getArsetBytes(int index) {
      return arset_.getByteString(index);
    }
    /**
     * <code>repeated string arset = 3;</code>
     * @param index The index to set the value at.
     * @param value The arset to set.
     * @return This builder for chaining.
     */
    public Builder setArset(
        int index, java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  ensureArsetIsMutable();
      arset_.set(index, value);
      onChanged();
      return this;
    }
    /**
     * <code>repeated string arset = 3;</code>
     * @param value The arset to add.
     * @return This builder for chaining.
     */
    public Builder addArset(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  ensureArsetIsMutable();
      arset_.add(value);
      onChanged();
      return this;
    }
    /**
     * <code>repeated string arset = 3;</code>
     * @param values The arset to add.
     * @return This builder for chaining.
     */
    public Builder addAllArset(
        java.lang.Iterable<java.lang.String> values) {
      ensureArsetIsMutable();
      com.google.protobuf.AbstractMessageLite.Builder.addAll(
          values, arset_);
      onChanged();
      return this;
    }
    /**
     * <code>repeated string arset = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearArset() {
      arset_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      bitField0_ = (bitField0_ & ~0x00000001);
      onChanged();
      return this;
    }
    /**
     * <code>repeated string arset = 3;</code>
     * @param value The bytes of the arset to add.
     * @return This builder for chaining.
     */
    public Builder addArsetBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      ensureArsetIsMutable();
      arset_.add(value);
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


    // @@protoc_insertion_point(builder_scope:AssociateOp)
  }

  // @@protoc_insertion_point(class_scope:AssociateOp)
  private static final gov.nist.csd.pm.pap.op.graph.proto.AssociateOp DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new gov.nist.csd.pm.pap.op.graph.proto.AssociateOp();
  }

  public static gov.nist.csd.pm.pap.op.graph.proto.AssociateOp getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<AssociateOp>
      PARSER = new com.google.protobuf.AbstractParser<AssociateOp>() {
    @java.lang.Override
    public AssociateOp parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new AssociateOp(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<AssociateOp> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<AssociateOp> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public gov.nist.csd.pm.pap.op.graph.proto.AssociateOp getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

