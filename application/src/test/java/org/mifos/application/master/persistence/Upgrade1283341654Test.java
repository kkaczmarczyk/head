/*
 * Copyright (c) 2005-2010 Grameen Foundation USA
 *  All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 *  See also http://www.apache.org/licenses/LICENSE-2.0.html for an
 *  explanation of the license and how it is applied.
 */

package org.mifos.application.master.persistence;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.application.questionnaire.migration.QuestionnaireMigration;
import org.mifos.customers.surveys.business.Survey;
import org.mifos.customers.surveys.helpers.SurveyType;
import org.mifos.customers.surveys.persistence.SurveysPersistence;
import org.mifos.framework.exceptions.PersistenceException;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mifos.customers.surveys.business.SurveyUtils.getSurvey;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class Upgrade1283341654Test {

    @Mock
    private QuestionnaireMigration questionnaireMigration;

    @Mock
    private SurveysPersistence surveysPersistence;

    @Mock
    private Connection connection;

    private Upgrade1283341654 upgrade1283341654;

    private Calendar calendar;

    @Before
    public void setUp() {
        upgrade1283341654 = new Upgrade1283341654(questionnaireMigration, surveysPersistence);
        calendar = Calendar.getInstance();
    }

    @Test
    public void shouldMigrateViewClientSurveys() throws PersistenceException, IOException, SQLException {
        Survey survey1 = getSurvey("Sur1", "Ques1", calendar.getTime());
        Survey survey2 = getSurvey("Sur2", "Ques2", calendar.getTime());
        List<Survey> surveys = asList(survey1, survey2);
        when(surveysPersistence.retrieveSurveysByType(SurveyType.CLIENT)).thenReturn(surveys);
        when(questionnaireMigration.migrateSurveys(surveys)).thenReturn(asList(1111));
        upgrade1283341654.upgrade(connection);
        verify(surveysPersistence).retrieveSurveysByType(SurveyType.CLIENT);
        verify(questionnaireMigration).migrateSurveys(surveys);
    }

}
