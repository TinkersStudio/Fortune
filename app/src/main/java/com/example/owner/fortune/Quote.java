package com.example.owner.fortune;

import java.util.*;

/**
 * Created by Dung Trinh on 6/20/2016.
 */
public class Quote {
    /** The main content of the quote*/
    private String content;

    public Quote (String content)
    {
        this.content = content;
    }

    public String toString()
    {
        return this.content;
    }

    public boolean equals(Objects obj)
    {
        if (obj == null || !this.content.equals(obj.toString()))
        {
            return false;
        }
        return true;
    }

}
