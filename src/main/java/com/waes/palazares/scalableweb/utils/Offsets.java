package com.waes.palazares.scalableweb.utils;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Offsets {
    public static String getOffsetsMessage(@NonNull byte[] left, @NonNull byte[] right){
        if(left.length < 1 || right.length < 1 || left.length != right.length){
            throw new UnsupportedOperationException("Can't compare offsets with zero or different length");
        }
        List<Offset> offsets = new ArrayList<>();
        int startIndex = 0;
        int curLength = left[0] == right[0] ? 0 : 1;

        for (int i = 1; i < left.length; i++) {
            if (left[i] != right[i]) {
                if(left[i-1] == right[i-1]){
                    startIndex = i;
                }
                curLength++;
            }
            //left[i] == right[i]
            else {
                if(left[i-1] != right[i-1]){
                    offsets.add(new Offset(startIndex, curLength));
                    curLength = 0;
                    startIndex = 0;
                }
            }
        }

        if(startIndex != 0 && curLength != 0){
            offsets.add(new Offset(startIndex, curLength));
        }

        return "Offsets [(index,length),..] : [" + String.join(",", offsets.stream().map(Offset::toString).toArray(String[]::new)) + "]";
    }

    @Data
    private static class Offset{
        private final int startIndex;
        private final int length;

        @Override
        public String toString() {
            return "(" + startIndex +
                    ", " + length + ')';
        }
    }
}
