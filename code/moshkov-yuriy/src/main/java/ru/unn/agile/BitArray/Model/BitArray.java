package ru.unn.agile.BitArray.core;

import java.util.Arrays;

public class BitArray {
    private final Boolean[] arrBit;
    private final int size;

    public BitArray(final int size) {
        this.arrBit = new Boolean[size];
        Arrays.fill(this.arrBit, false);
        this.size = size;
    }

    public Boolean getBit(final int index) {
        return arrBit[index];
    }

    public void setBit(final int index, final Boolean value) {
        if (value == null) {
            return;
        }
        arrBit[index] = value;
    }

    public void setAll(final Boolean value) {
        Arrays.fill(this.arrBit, value);
    }

    public BitArray and(final BitArray bitArray) {
        return this.doOperationWithArr(bitArray, new AndOperation());
    }

    public BitArray or(final BitArray bitArray) {
        return this.doOperationWithArr(bitArray, new OrOperation());
    }

    public BitArray xor(final BitArray bitArray) {
        return this.doOperationWithArr(bitArray, new XorOperation());

    }

    public BitArray not() {
        BitArray res = new BitArray(this.size);
        for (int i = 0; i < this.size; i++) {
            res.setBit(i, !this.getBit(i));
        }
        return res;
    }

    public Boolean[] getArrBit() {
        return this.arrBit;
    }

    public int getSize() {
        return this.size;
    }

    private BitArray doOperationWithArr(final BitArray bitArray, final IOperation operation) {
        BitArray res;
        if (bitArray.getSize() == this.size) {
            res = new BitArray(this.size);
            for (int i = 0; i < this.size; i++) {
                res.setBit(i, operation.doOperation(this.getBit(i), bitArray.getBit(i)));
            }
        } else {
            throw new BitArrayDifferentSizeException("Different size of arrays");
        }
        return res;
    }

    private class XorOperation implements IOperation {
        @Override
        public Boolean doOperation(final Boolean first, final Boolean second) {
            return first ^ second;
        }
    }

    private class OrOperation implements IOperation {
        @Override
        public Boolean doOperation(final Boolean first, final Boolean second) {
            return first | second;
        }
    }

    private class AndOperation implements IOperation {
        @Override
        public Boolean doOperation(final Boolean first, final Boolean second) {
            return first & second;
        }
    }
}

