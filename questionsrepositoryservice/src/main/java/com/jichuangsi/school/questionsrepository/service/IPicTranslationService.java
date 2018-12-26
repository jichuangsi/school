package com.jichuangsi.school.questionsrepository.service;

import com.jichuangsi.school.questionsrepository.model.translate.PicContent;

public interface IPicTranslationService {

    PicContent translate(byte[] content);
}
